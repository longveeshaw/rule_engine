package engine

/**
 * Author: monkey 
 * Date: 2017/8/21
 */
interface BaseFieldRuleEngine {

    def db(Map map)

    def rpc(Map map)

    def http(Map map)

    def range(Map map)

    def calculation(Map map)

}