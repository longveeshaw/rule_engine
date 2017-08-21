package config

import parse.RangeDSL

/**
 * Author: monkey 
 * Date: 2017/8/21
 * 定义了一个规则下多个条件的字段
 */
class FieldRule {

    def getRuleFields() {
        println "假设通过参数Map从数据库中获取规则"
        def rule = [
                '_id'        : 'rule_21_score',
                'desc'       : '征信分数计算',
                'rule_id'    : 'rule_21',
                'field'      : 'score',
                'enable'     : 1,
                'timestamp'  : new Date().getTime(),
                'last_modify': new Date().getTime(),
                // 这个字段也是动态生成,数据源[db,rpc,http,range,calculation],
                'rules'      : [
                        ['range': '(1-10]=5,(11-40]=30,(41-100]=90'],
                        ['calculation': 'LOG(2,score)']
                ]
        ]

        rule
    }

    public static void main(String[] args) {
        Map params = ['score': 50]
        FieldRule fieldRule = new FieldRule()
        Map fieldCondition = fieldRule.getRuleFields()
        def field = fieldCondition['field']
        if (!params.containsKey(field)) {
            println '不存在的field'
            return
        }
        def initValue = params[field]
        List<Map> rules = fieldCondition.containsKey('rules') ? fieldCondition['rules'] : []
        if (rules.size() == 0) {
            println '不存在rules'
            return
        }

        def binding = new Binding()
        def shell = new GroovyShell(binding)
        def script = ''
        rules.each {
            it ->
                // ['range': '(1-10]=5,(11-40]=30,(41-100]=90'] || ['calculation': 'LOG(2,score)']
                it.each {
                    k, v ->
                        if (k == 'range') {
                            println '需要解析dsl,并且将score的值再次赋值,需要上下文对应'
                            script = RangeDSL.parseRangeDSL(v, field,initValue)
                        }
                }
        }
        println  shell.evaluate(script)


    }
}