package parse

import groovy.text.GStringTemplateEngine

/**
 * Author: monkey 
 * Date: 2017/8/21
 * 根据传进来的 range : dsl 去解析
 * 通过解析dsl 生成 script 运算 赋值返回
 *
 *
 */
class RangeDSL {
    // (1-10]=5,(11-40]=30,(41-100]=90
    def range(rangeExpression, field) {
        def tempalte = new StringBuffer()
        def ranges = rangeExpression.split(',')
        ranges.each {
            curr ->
                // (1-10]=5 的解析
                tempalte.append(RangeDSL.createRangeTempate(curr, field))
        }

//        tempalte.append("\n").append("return ${field}")
        tempalte.toString()
    }

    static createRangeTempate(String rangeNode, String field) {
        // (1-10]=5
        def expression = rangeNode.split('=')
        def lhs = expression[0]
        //(1-10] 去括号
        def lessNumber = lhs.substring(1, lhs.indexOf('-'))
        def largeNumber = lhs.substring(lhs.indexOf('-') + 1, lhs.length() - 1)
        def value = expression[1]
        def f = new File('/Users/monkey/work/ideaWorkSpace/rule_engine/src/template/range.template')
        def engine = new GStringTemplateEngine()
        def binding = [
                lessNumber : lessNumber,
                largeNumber: largeNumber,
                value      : value,
                field      : field
        ]
        def template = engine.createTemplate(f).make(binding)
        template.toString()
    }

    def a() {
        println "123"
    }

    static void main(String[] args) {

//         RangeDSL.parseRangeDSL(rangeNode,'score')
    }
}