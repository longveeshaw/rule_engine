package enums;

/**
 * Author: monkey
 * Date: 2017/8/21
 */
public enum CalculationOperator {
    // LOG(底数,底数的N次方的值) LOG(2,8) => 3
    LOG("createLogTemplate"),
    // ADD('column') 加上column,也可以是一个自然数
    ADD("createAddTemplate"),
    SUBTRACT("createSubtractTemplate"),
    MULTIPLY("createMultiplyTemplate"),
    DIVIDE("createDivideTemplate");

    private String methodName;

    private CalculationOperator(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
