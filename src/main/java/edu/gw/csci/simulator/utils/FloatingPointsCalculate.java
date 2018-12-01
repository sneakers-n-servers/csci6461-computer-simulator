package edu.gw.csci.simulator.utils;

import java.util.BitSet;

public class FloatingPointsCalculate {
    private static int count = 0;
    private String f1,f2;
    boolean add_mius; //true = add; false = mius;
    private String Sign1,Sign2;
    private String exponent1,exponent2,mantissa1,mantissa2;
    private String result;
    private String exponentResult;
    private String mantissaResult;
    private String normalizedMantissa;
    private String mantissa1WithSign,mantissa2WithSign;
    private boolean OVERFLOW;
    private boolean UNDERFLOW;
    private String fpncName;


    public FloatingPointsCalculate(String f1, String f2, boolean add_mius){
        this.fpncName = "Floating Points Number Calculator: "+String.valueOf(++count);
        this.f1=f1;
        this.f2=f2;
        this.add_mius = add_mius;
        initialize();
    }
    public FloatingPointsCalculate(BitSet bit1,BitSet bit2,boolean add_mius){
        this.f1 = BitConversion.toBinaryString(bit1,16);
        this.f2 = BitConversion.toBinaryString(bit2,16);
        this.add_mius = add_mius;
        initialize();
    }

    private void initialize(){
        this.Sign1 = f1.substring(0,1);
        this.Sign2 = f2.substring(0,1);
        this.exponent1 = f1.substring(1,1+ FloatingPointConvert.EXPONENT_BIT);
        this.exponent2 = f2.substring(1,1+ FloatingPointConvert.EXPONENT_BIT);
        //规格化浮点数的有效值1.M，在尾数前加1
        this.mantissa1 = "1"+f1.substring(FloatingPointConvert.FLOATING_POINT_BIT- FloatingPointConvert.MANTISSA_BIT);
        this.mantissa2 = "1"+f2.substring(FloatingPointConvert.FLOATING_POINT_BIT- FloatingPointConvert.MANTISSA_BIT);

        //求补,取反再+1，双符号位判断溢出
        if(Sign1.equals("1")){
            this.mantissa1WithSign = "11"+BinaryCalculate.BinaryAddOne(BinaryCalculate.flip(mantissa1));
        }
        else {
            this.mantissa1WithSign = "00" + mantissa1;
        }
        if(Sign2.equals("1")){
            this.mantissa2WithSign = "11"+BinaryCalculate.BinaryAddOne(BinaryCalculate.flip(mantissa2));
        }
        else {
            this.mantissa2WithSign = "00" + mantissa2;
        }
    }

    public String getFpncName(){
        return fpncName;
    }
    public void show(){
        String mess1 = String.format("F1--S:%s E:%s(%d) M:%s",
                Sign1,
                exponent1, Integer.parseInt(exponent1,2),
                mantissa1
                );
        String mess2 = String.format("F2--S:%s E:%s(%d) M:%s",
                Sign2,
                exponent2, Integer.parseInt(exponent2,2),
                mantissa2
        );

        System.out.println(mess1);
        System.out.println(mess2);
    }

    public BitSet getResult(){
        new Result(new Normalize(new Calculate(new Match())));
        return BitConversion.convert(result);
    }
    public boolean isOVERFLOW(){
        return OVERFLOW;
    }
    public boolean isUNDERFLOW(){
        return UNDERFLOW;
    }

    public class Match{
        private int exponent1_,exponent2_;
        private String Name;

        public Match(){
            this.Name = String.format("Match: Stage 1 for %s",getFpncName());
            getName();
            this.exponent1_ = Integer.parseInt(exponent1,2);
            this.exponent2_ = Integer.parseInt(exponent2,2);
            MatchExponent();
        }
        public void MatchExponent(){
            if(exponent1_>exponent2_){
                //对阶，小阶向大阶看齐，尾数右移n位，阶码+n
                mantissa2WithSign = BinaryCalculate.BinaryArithmeticalRightShift(mantissa2WithSign,exponent1_-exponent2_);
                exponentResult = exponent1;

            }
            else if(exponent1_<exponent2_){
                mantissa1WithSign = BinaryCalculate.BinaryArithmeticalRightShift(mantissa1WithSign,exponent2_-exponent1_);
                exponentResult = exponent2;
            }
            else {
                exponentResult = exponent1;
            }
        }
        public String getName(){
            return Name;
        }
    }
    public class Calculate{
        private String Name;
        public Calculate(Match match){
            this.Name = String.format("Calculate: Stage 2 for %s",getFpncName());
            getName();
            if(add_mius){
                Add();
            }
            else {
                Minus();
            }
        }
        public String getName(){
            return Name;
        }
        public void Add(){
            mantissaResult = BinaryCalculate.BinaryAdd(mantissa1WithSign,mantissa2WithSign);
            System.out.println("Add");
            //getMantissaResult();
        }
        public void Minus(){
            mantissaResult = BinaryCalculate.BinaryMinus(mantissa1WithSign,mantissa2WithSign);
            System.out.print("Minus");
            //getMantissaResult();
        }
        private void getMantissaResult(){
            System.out.println(String.format("mantissa1：%s",mantissa1WithSign));
            System.out.println(String.format("mantissa2：%s",mantissa2WithSign));
            System.out.println(String.format("add/mius result：%s",mantissaResult));
        }
    }
    public class Normalize{
        private String Name;
        private String doubleSign;
        public Normalize(Calculate calculate){
            this.Name = String.format("Normalize: Stage 3 for %s",getFpncName());
            getName();
            this.doubleSign = mantissaResult.substring(0,2);
            normalizedMantissa = mantissaResult;
            FloatNormalize();
        }
        public String getName(){
            return Name;
        }
        public void FloatNormalize(){
            // If the two sign bit data of the mantissa addition result are not equal,
            // it indicates that the absolute value of the mantissa of the operation result is greater than 1.
            // So we need to "normalize to the right". Since the absolute value of the mantissa addition cannot exceed 2,
            // normalization to the right is definitely the right hand shift of 1 digit and the step code plus 1.
            if(doubleSign.equals("01")||doubleSign.equals("10")){
                int temp = Integer.parseInt(exponentResult,2);
                System.out.println("normalize to the right");
                normalizedMantissa = BinaryCalculate.BinaryArithmeticalRightShift(normalizedMantissa);//Right Shift 1
                exponentResult=BinaryCalculate.BinaryAddOne(exponentResult);//exponent +1
                if(Integer.parseInt(exponentResult,2)!=temp+1){
                    //exponent overflow
                    OVERFLOW = true;
                }
            }
            //If the sign bit of the mantissa addition result is equal to the highest bit of the data,
            // it indicates that the data is not normalized, and the mantissa should be "normalized to the left",
            // that is, the mantissa is shifted to the left by n bits, and the order code is decremented by n.
            else if(normalizedMantissa.charAt(1)==normalizedMantissa.charAt(2)){
                System.out.println("normalized to the left");
                while (normalizedMantissa.charAt(1)==normalizedMantissa.charAt(2)){
                    int temp = Integer.parseInt(exponentResult,2);
                    normalizedMantissa = BinaryCalculate.BinaryLeftShift(normalizedMantissa);//Left Shift 1
                    exponentResult=BinaryCalculate.BinaryMinusOne(exponentResult);//exponent -1
                    if(Integer.parseInt(exponentResult,2)!=temp-1){
                        //exponent underflow
                        UNDERFLOW = true;
                    }
                }
            }
            else {
                System.out.println("The result is already a normalized floating points number.");
            }
        }
    }
    public class Result{
        private String Name;
        private char ResultSign;
        public Result(Normalize normalize){
            this.Name = String.format("Result: Stage 4 for %s",getFpncName());
            getName();
            this.ResultSign = normalizedMantissa.charAt(0);
            if(ResultSign=='0'){
                result = ResultSign+exponentResult+normalizedMantissa.substring(3);
            }
            else {
                result = ResultSign+exponentResult+
                        BinaryCalculate.flip(BinaryCalculate.BinaryMinusOne(normalizedMantissa.substring(3)));
            }
            showResult();
        }

        public String getName(){
            return Name;
        }
        public void showResult(){
            System.out.println(result);
            System.out.println(FloatingPointConvert.FloatConvert(result));
        }
    }
}
