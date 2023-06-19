// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your cod
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.println("Привет! Добро пожаловать в калькулятор!");

        //Все возможные символы (база символов)
        char[] decimalDigits = {'0','1','2','3','4','5','6','7','8','9'};
        char[] arrayOperator = {'+','-','/','*'};
        char[] romanNumerals = {'I','V','X'};
        // Строки в которые записываем найденые символы из введенного выражения
        String stringNumberOne;
        String stringNumberTwo;

        int numberOne = 0;
        int numberTwo = 0;
        boolean romanSystem = false;// если нашли римский цифру, запишем true
        boolean nextNumber = false;// закрываем запись в stringNumberOne и открываем запись в stringNumberTwo, как получим true
        boolean nextCycle;// пропускаем внутренние циклы если символ уже найден
        char operator;
        // в нутри данного цикла осуществляется поиск и запись символов
        // систем счисления в переменные дял дальнейшего выполнения выражения

        System.out.println("Калькулятор может принимать числа от 1 до 10 включительно");
        System.out.println("Введите выражение.");
        String expression = in.nextLine();
        expression.trim();
        stringNumberOne = "";
        stringNumberTwo = "";
        operator = '.';

        // Условия при которых выбрасывается исключения
        if(expression.indexOf('-') == -1 && expression.indexOf('+') == -1 &&
                expression.indexOf('*') == -1 && expression.indexOf('/') == -1){
            throw new Exception("//т.к. строка не является математической операцией.");
        }

        if (expression.matches("(I|V|X)+\s*[-+*/]\s*([1-9]|10)")||
                expression.matches("([1-9]|10)\s*[-+*/]\s*(I|V|X)+"))
            throw new Exception("//т.к. используются одновременно разные системы счисления");

        //Данное условие обрабатывает все возможные исключения из условий задания, но по условию задания на каждый случай
        //должно выбрасываться свое исключение.
        if (!(expression.matches("(I|II|III|IV|V|VI|VII|VIII|IX|X)\s*[-+*/]\s*(I|II|III|IV|V|VI|VII|VIII|IX|X)")||
                expression.matches("(1|2|3|4|5|6|7|8|9|10)\s*[-+*/]\s*(1|2|3|4|5|6|7|8|9|10)")))
            throw new Exception("Калькулятор должен принимать на вход числа от 1 до 10 включительно, не более.\n" +
                    "Калькулятор умеет работать только с целыми числами.\\n\" +\n" +
                    "//т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)\n");

        // в цикле осучествляется посимвольное присвоение из введенной строки в строки stringNumberOne, stringNumberTwo
        // и в переменную operator
        for (int i = 0; i < expression.length(); i++)
        {
            if (expression.charAt(i) == ' ')
                continue;

            nextCycle = false;
            // Проверка на римские символы
            for (int j = 0; j < romanNumerals.length; j++){
                if(expression.charAt(i) == romanNumerals[j]){
                    if (!nextNumber)
                        stringNumberOne += expression.charAt(i);
                    else
                        stringNumberTwo += expression.charAt(i);
                    romanSystem = true; // используется римская система счисления
                    nextCycle = true; // символ найден, поиск в других циклах не требуется
                    break;
                }
            }
            if(!nextCycle) {
                for (int j = 0; j < decimalDigits.length; j++) {
                    if (expression.charAt(i) == decimalDigits[j]) {
                        if (!nextNumber)
                            stringNumberOne += expression.charAt(i);
                        else
                            stringNumberTwo += expression.charAt(i);
                        break;
                    }
                }
            }
            if(!nextCycle) {
                for (int j = 0; j < arrayOperator.length; j++) {
                    if (expression.charAt(i) == arrayOperator[j]) {
                        operator = expression.charAt(i);
                        nextNumber = true; // символ является оператором, следующие символы строки относятся ко второму чмслу
                        break;
                    }
                }
            }
        }
        //перевод римских чисел в арабские для выполнения выражения.
        if(romanSystem){
            TranslationSystemNumeral translationSystemNumeral = new TranslationSystemNumeral();
            numberOne = translationSystemNumeral.InArabSystemNumeral(stringNumberOne);
            numberTwo = translationSystemNumeral.InArabSystemNumeral(stringNumberTwo);
        }
        else {
            numberOne = Integer.parseInt(stringNumberOne);
            numberTwo = Integer.parseInt(stringNumberTwo);
        }

        int result = 0;
        switch(operator){
            case '-':
                result = numberOne - numberTwo;
                break;
            case '+':
                result = numberOne + numberTwo;
                break;
            case '*':
                result = numberOne * numberTwo;
                break;
            case '/':
                result = numberOne / numberTwo;
                break;
        }

        if (romanSystem) {
            if (result <= 0){
                throw new RuntimeException("т.к. в римской системе нет отрицательных чисел");
            }
            TranslationSystemNumeral translationRomanNumeral = new TranslationSystemNumeral();
            System.out.println("= " + translationRomanNumeral.InRomanSystemNumeral(result));
        }
        else {
            System.out.println("= " + result);
        }
    }
    public static class TranslationSystemNumeral {
        RomanNumeral[] translationRomanNumeral =
                {RomanNumeral.I, RomanNumeral.IV, RomanNumeral.V,
                        RomanNumeral.IX, RomanNumeral.X, RomanNumeral.XL,
                        RomanNumeral.L, RomanNumeral.XC, RomanNumeral.C};
        TranslationRomaInArab[] translationRomaInArabs =
                {TranslationRomaInArab.I, TranslationRomaInArab.V, TranslationRomaInArab.X};
        String InRomanSystemNumeral(int numberInt) {
            int i = translationRomanNumeral.length - 1;
            String resultRomaNumber = "";
            while (numberInt > 0) {
                // ищем число в Enum меньшее или равное результату выражения,
                // просматриваем массив с последнего элемента.
                while (translationRomanNumeral[i].getTranslation() > numberInt)
                    i--;
                resultRomaNumber += translationRomanNumeral[i].name();
                numberInt -= translationRomanNumeral[i].getTranslation();
            }
            return resultRomaNumber;
        }

        // перевод в арабске числа
        int InArabSystemNumeral(String numberString) {
            int result = 0;
            int i = 2;
            int j = 0;
            int prevValues = 0;
            while (j < numberString.length()) {
                //сравниваем подстроку numberString с елементами перечисления TranslationRomaInArabs
                // при совподении выходим из цикла
                while(!(numberString.substring(j,translationRomaInArabs[i].name().length()+j).equals(translationRomaInArabs[i].name())))
                {
                    if (i == 0)
                        i = 3;
                    i--;
                    if (i==0){break;}
                }

                //int prevValues - дает возможность сравнить текущее значение с прошлым
                //чтобы выполнять вычитание в том случае, если prevValues больше прошлого значения -> (IX)
                if (result == 0 || prevValues >= translationRomaInArabs[i].getTranslation())
                    result += translationRomaInArabs[i].getTranslation();
                else
                    result = translationRomaInArabs[i].getTranslation() - result;
                j += translationRomaInArabs[i].name().length();
                prevValues = translationRomaInArabs[i].getTranslation();
            }
            return result;
        }
    }
}