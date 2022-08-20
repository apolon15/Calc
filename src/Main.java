
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        boolean stop;
        do {
            stop = true;
            Scanner sc = new Scanner(System.in);
            LinkedList<Double> listNumb = new LinkedList<Double>();
            LinkedList<Character> listChar = new LinkedList<Character>();
            StringBuffer sb = new StringBuffer();
            System.out.println("Введи строку для расчета");
            String userInput = sc.nextLine();
            String patternTest = ".*\\(?\\d+\\.*[*+-/].*\\d+\\)?";
            Pattern pTest = Pattern.compile(patternTest);
            Matcher mTest = pTest.matcher(userInput);
            if (!mTest.matches()) {//проверка на пустую строку
                System.out.println("Строка не валидна");
            } else {
                String exemp2 = userInput.replaceAll("[^*+-/()%√.,\\d]", "");//удалил все кроме чисел и математических знаков.
                String pattern = new String("\\(\\d+[*+-/]\\d+\\)");//регулярное выражение
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(exemp2);
                String part = null;//пустая строка

                //ДЛЯ РАСЧЕТОВ, ЕСЛИ ЕСТЬ СКОБКИ

                if (m.find()) {//проверка наличия выражения в скобках
                    LinkedList<Double> listNumbPart = new LinkedList<Double>();
                    LinkedList<Character> listCharPart = new LinkedList<Character>();
                    part = exemp2.substring(exemp2.indexOf("(") + 1, exemp2.lastIndexOf(")"));//строка,находящаяся в скобках
                    String testPart[] = getDigit(part);
                    String testCharPart[] = getMath(part);
                    for (int i = 0; i < testPart.length; i++) {
                        if (testPart[i] != "") {
                            listNumbPart.add(Double.parseDouble(testPart[i])); //преобразую числа в цифры
                        }
                    }
                    for (int i = 0; i < testCharPart.length; i++) {
                        if (testCharPart[i] != "") {//преобразую математические знаки в char и помещаю в массив listChar
                            listCharPart.add(testCharPart[i].charAt(0));
                        }
                    }
                    //начало математических действий.Первоочередное умножение и деление
                    double result = listNumbPart.get(0);//результат-первое число с которого начинаем.
                    for (int i = 0; i < listCharPart.size(); i++) {
                        boolean bool;
                        if ('*' == listCharPart.get(i)) {//если '*' -предпринимаем действие
                            bool = true;
                            result = mult_div(listNumbPart.get(i - 1), listNumbPart.get(i), true);//в одном методе умножение и деление.
                            //в зависимости какое значение имеет bool;
                            listNumbPart.set(i, result);//перезаписываем результат и смещаем в индексе
                            listNumbPart.remove(i - 1);//удаляем отработанное число
                        }
                        if ('/' == listCharPart.get(i)) {//если '/' -предпринимаем действие
                            bool = false;
                            result = mult_div(listNumbPart.get(i - 1), listNumbPart.get(i), false);
                            listNumbPart.set(i, result);//перезаписываем результат и смещаем в индексе
                            listNumbPart.remove(i - 1);//удаляем отработанное число
                        }
                    }
                    boolean flag = true;
                    for (int i = 0; i < listCharPart.size(); i++) {
                        if (listCharPart.get(0) == '-' && flag && listChar.size() > 1) {//если первый знак "-",и знаков более 1, то число после него * -1 что бы сделать его отрицательным
                            listNumbPart.set(0, listNumb.get(i) * -1);//flag для того что бы использовать только единажды IF
                            listCharPart.remove(0);//удаляем отработанный знак
                            flag = false;
                        }
                        if ('+' == listCharPart.get(i)) {//если '+' -предпринимаем действие
                            result = sum(listNumbPart.get(i), listNumbPart.get(i + 1));
                            listNumbPart.set(i + 1, result);//перезаписываем результат и смещаем в индексе
                            listNumbPart.set(i, 0.0);//обнуляю отработанное число
                        }
                        if ('-' == listCharPart.get(i)) {
                            result = minus(listNumbPart.get(i), listNumbPart.get(i + 1));
                            listNumbPart.set(i + 1, result);//удаляем отработанное число
                            listNumbPart.set(i, 0.0);//обнуляю отработанное число
                        }
                    }//решеное в скобках преобразую с строку и заменяю в ориг.строке.
                    String strResult = String.valueOf(result);
                    sb = sb.append(exemp2.substring(0, exemp2.indexOf("(")));
                    sb = sb.append(strResult);
                    exemp2 = sb.toString();
                }


                String test[] = getDigit(exemp2);//поместил в массив только числа;
                String testChar[] = getMath(exemp2);//поместил в массив только знаки;

                for (int i = 0; i < test.length; i++) {
                    if (test[i] != "") {
                        listNumb.add(Double.parseDouble(test[i])); //преобразую числа в цифры
                    }
                }
                for (int i = 0; i < testChar.length; i++) {
                    if (testChar[i] != "") {//преобразую математические знаки в char и помещаю в массив listChar
                        listChar.add(testChar[i].charAt(0));
                    }
                }
                //необходимый "велосипед" для того что бы отрабатывало '*'  и '/'
                /*вроде как обошелся без него*/

//                boolean flagForPlus_Minus = false;//для проверки на присутствие знаков '/' и '*' ;
//                for (char el : listChar) {
//                    if (el == '/' || el == '*') {
//                        flagForPlus_Minus = true;
//                    }
//                }

                //начало математических действий.Первоочередное умножение и деление
                double result = listNumb.get(0);//результат-первое число с которого начинаем.
                for (int i = 0; i < listChar.size(); i++) {
                    boolean bool;
                    if (listChar.size() > 0 && '*' == listChar.get(i)) {// если следующий знак '*' произведение
                        bool = true;
                        result = mult_div(listNumb.get(i), listNumb.get(i + 1), true);
                        listNumb.set(i + 1, result);
                        listNumb.remove(i);
//                        listChar.remove(i);
                    }
                    if (listChar.size() > 0 && '/' == listChar.get(i)) {//если следующий знак '/' деление
                        bool = false;
                        result = mult_div(listNumb.get(i), listNumb.get(i + 1), false);
                        listNumb.set(i + 1, result);
                        listNumb.remove(i);
                        listChar.remove(i);
                    }
//                    if (flagForPlus_Minus) {//необходимый "велосипед" для того что бы отрабатывало '*'  и '/'
//                        i--;
//                    }
                }
                boolean flag = true;
                for (int i = 0; i < listChar.size(); i++) {
                    if (listChar.get(0) == '-' && flag && listChar.size() > 1) {//если первый знак "-", то число после него * -1 что бы сделать его отрицательным
                        listNumb.set(0, listNumb.get(i) * -1);//flag для того что бы использовать только единажды IF
                        listChar.remove(0);
                        flag = false;
                    }
                    if ('+' == listChar.get(i)) {//если следующий знак '+'
                        result = sum(listNumb.get(i), listNumb.get(i + 1));
                        listNumb.set(i + 1, result);
                        listNumb.set(i, 0.0);
                    }
                    if ('-' == listChar.get(i)) {//если следующий знак '-'
                        result = minus(listNumb.get(i), listNumb.get(i + 1));
                        listNumb.set(i + 1, result);
                        listNumb.set(i, 0.0);
                    }
                }
                System.out.println(result);
            }
            System.out.println("Завершить: жми 0");
            System.out.println("Продолжить:любая клавиша");
            String temp = sc.next();
            try {
                int x = Integer.parseInt(temp);
                if (x == 0) {
                    stop = false;
                }
            } catch (NumberFormatException ex) {

            }
        } while (stop);
    }

    public static double minus(double result, double digit) {//вычитание
        return result - digit;
    }

    public static double mult_div(double result, double digit, boolean bool) {//умножение либо деление.Зависит от true/false
        if (bool == true) {
            result = result * digit;//умножение
        }
        if (bool == false) {
            result = result / digit;//деление
        }
        return result;
    }

    public static double sum(double result, double digit) {//сложение
        return result + digit;
    }

    public static String[] getDigit(String str) {//получение цифр
        //была идея, но затерялась в мыслях
        double x = 0;
        if (str.contains("+-") || str.contains("*-") || str.contains("/-")) {
            Pattern p = Pattern.compile("(\\-\\d+)");
            Matcher m = p.matcher(str);
            if (m.find()) {
                x = Double.parseDouble(m.group(0));
            }
        }
        if (str.contains("--")) {
            Pattern p = Pattern.compile("(\\-\\d+)");
            Matcher m = p.matcher(str);
            if (m.find()) {
                x = Double.parseDouble(m.group(0)) * -1;
            }
        }
        String test[] = str.split("[/*+-]-?");
        return test;
    }

    public static String[] getMath(String str) {//получение математических знаков
        //TO DO для корректной работы с отрицательным числом. Идея пока не реализовалась
        if (str.contains("+-") || str.contains("*-") || str.contains("/-") || str.contains("--")) {


        }
        String test[] = str.split("\\d+\\.?\\d{0,}");

        return test;
    }

    //получение строки в нужном формате
    public static String getStr(String str) {
        str = str.replaceAll("[^*+-/()%√.,\\d]", "");
        return str;
    }
}