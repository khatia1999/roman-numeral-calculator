import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    public Map<String, Integer> romanNumerals = new HashMap<>();

    public Calculator() {
        romanNumerals.put("I", 1);
        romanNumerals.put("V", 5);
        romanNumerals.put("X", 10);
        romanNumerals.put("L", 50);
        romanNumerals.put("C", 100);
    }

    private int getNumberOfSubStringsInWord(String word, String subString){
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = word.indexOf(subString, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += subString.length();
            }
        }
        return count;
    }

    private boolean isRomanNumeral(String romanNumeral){
        Pattern pattern = Pattern.compile("[^IVXLC]");
        Matcher matcher = pattern.matcher(romanNumeral);
        boolean matchFound = matcher.find();
        return !matchFound;
    }

    private boolean checkIfNumberIsValid(String romanNumeral){
        if (romanNumeral.chars().filter(ch -> ch=='V').count() > 1
                || romanNumeral.chars().filter(ch -> ch=='L').count() > 1
                || romanNumeral.chars().filter(ch -> ch=='I').count() > 3
                || romanNumeral.chars().filter(ch -> ch=='C').count() > 1
                || (romanNumeral.chars().filter(ch -> ch=='X').count()>3
                && !romanNumeral.contains("IX"))
                || getNumberOfSubStringsInWord(romanNumeral, "IX")>1){
            return false;
        }

        Pattern pattern = Pattern.compile("[I]+[C]|[I]+[L]|[I]+[X]+[V]|[V]+[XLC]|[L]+[C]");
        Matcher matcher = pattern.matcher(romanNumeral);
        boolean matchFound = matcher.find();
        if (matchFound) {
            return false;
        }

        for (int i=0; i<romanNumerals.size(); i++) {
            var charact = romanNumerals.keySet().toArray()[i].toString();
            if (getNumberOfSubStringsInWord(romanNumeral, charact) > 1) {
                var firstSymbolIndex = romanNumeral.indexOf(charact);
                var lastSymbolIndex = romanNumeral.lastIndexOf(charact);
                var symbolValue = romanNumerals.get(charact);
                String secondSymbol = String.valueOf(romanNumeral.charAt(firstSymbolIndex + 1));
                String previousSymbol = String.valueOf(romanNumeral.charAt(lastSymbolIndex - 1));
                var secondSymbolValue = romanNumerals.get(secondSymbol);
                var previousSymbolValue = romanNumerals.get(previousSymbol);
                if ((secondSymbolValue >= symbolValue && previousSymbolValue > symbolValue)
                        || (secondSymbolValue > symbolValue && previousSymbolValue >= symbolValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    private int calculateValue(String numeral) {
        int value = 0;
        //0 => I  1 => X
        String[] parts = numeral.split("");
        Integer[] numbers = new Integer[numeral.length()];
        for (int i=0; i < parts.length; i++) {
            numbers[i] = romanNumerals.get(parts[i]);
        }
        List<Integer> evaluatedNumbers = new ArrayList<>();
        for (int i = 0; i < numbers.length; i++) {
            if (i >= numbers.length - 1) {
                evaluatedNumbers.add(numbers[numbers.length - 1]);
            } else if (numbers[i] >= numbers[i+1]) {
                evaluatedNumbers.add(numbers[i]);
            } else {
                evaluatedNumbers.add(numbers[i+1] - numbers[i]);
                i++;
            }
        }
        for (Integer number : evaluatedNumbers) {
            value += number;
        }
        return value;
    }

    public static void main(String[] args) throws Exception {
        Calculator calculator = new Calculator();
        Scanner scanner =new Scanner(System.in);
        System.out.println("Please enter expression:");
        scanner.useDelimiter("exit");
        int counter = 1;
        List<Integer> numbers = new ArrayList<>();
        List<String> operators = new ArrayList<>();
        while(scanner.hasNext()) {
            String expression = scanner.nextLine().trim();
            if (counter%2==1){
                if (!calculator.isRomanNumeral(expression)){
                    throw new Exception("Please enter roman numerals");
                }else{
                    if (!calculator.checkIfNumberIsValid(expression)){
                        throw new Exception("Roman numeral is not correct");
                    }else{
                        numbers.add(calculator.calculateValue(expression));
                    }
                }
            }else if (!expression.equals("exit")){
                switch (expression.trim()){
                    case "+": operators.add("+");
                        break;
                    case "-": operators.add("-");
                        break;
                    case "*": operators.add("*");
                        break;
                    case "/": operators.add("/");
                        break;
                    case "%": operators.add("%");
                        break;
                    default: throw new Exception("Invalid operator");
                }
            }else{
                break;
            }
            counter++;
        }
        int result = numbers.get(0);
        for (int i=0; i<operators.size(); i++){
            switch (operators.get(i)) {
                case "+": result += numbers.get(i+1);
                    break;
                case "-": result -= numbers.get(i+1);
                    break;
                case "*": result *= numbers.get(i+1);
                    break;
                case "/": result /= numbers.get(i+1);
                    break;
                case "%": result %= numbers.get(i+1);
                    break;
            }
        }
        System.out.println("Result: "+ calculator.intToRoman(result));
    }
    public String intToRoman(int num) {
        String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds =
                {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens =
                {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] units =
                {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        return thousands[num / 1000] +
                hundreds[(num % 1000) / 100] +
                tens[(num % 100) / 10] +
                units[num % 10];
    }

}