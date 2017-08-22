package config

import parse.ParseEngine

/**
 * Author: monkey 
 * Date: 2017/8/21
 * 定义了一个规则下多个条件的字段
 */
class RuleDemo {

    // 前置规则，初始化规则的入参
    def getRuleFields() {
        println "假设通过参数Map从数据库中获取规则"
        def rule_field = [
                '_id'        : 'rule_21_score',
                'desc'       : '征信规则score字段计算规则',
                'rule_id'    : 'rule_21',
                'field'      : 'score',
                'enable'     : 1,
                'timestamp'  : new Date().getTime(),
                'last_modify': new Date().getTime(),
                'rules'      : [
                        ['range': '(1-10]=5,(11-40]=30,(41-100]=90'],
                        ['calculation': 'LOG(2,score)']
                ]
        ]

        rule_field
    }

    // 真正的规则，expression 代表验证逻辑
    def getRule(){
        def rule = [
                '_id'        : 'rule_21',
                'desc':'判断征信分数是否大于N',
                'expression' : 'score > 2'
        ]

        rule
    }

    public static void main(String[] args) {
        Map fact = ['score': 50]
        RuleDemo ruleDemo = new RuleDemo()
        Map fieldCondition = ruleDemo.getRuleFields()
        def field = fieldCondition['field']
        if (!fact.containsKey(field)) {
            println '不存在的field'
            return
        }
        def initValue = fact[field]
        List<Map> rules = fieldCondition.containsKey('rules') ? fieldCondition['rules'] : []
        if (rules.size() == 0) {
            println '不存在rules'
            return
        }

        def script = new StringBuffer("def ${field} = ${initValue} \n")
        rules.each {
            it ->
                it.each { k, v ->
                    def parseUtil = new ParseEngine()
                    script.append(parseUtil.invokeMethod(k, [v, field]))
                }
        }

        def rule = ruleDemo.getRule()
        def expression = rule.containsKey('expression') ? rule['expression'] : ''
        if(expression == ''){
            return
        }

        script.append("\n").append("return ${expression}")
        println script.toString()
        def binding = new Binding()
        def shell = new GroovyShell(binding)
        def value = shell.evaluate(script.toString())
        println value
    }
}