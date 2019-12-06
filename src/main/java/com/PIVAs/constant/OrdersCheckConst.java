package com.PIVAs.constant;

/**
 * @author li_yinghao
 * @version 1.0
 * @date 2019/12/6 15:25
 * @description 医嘱审查状态值
 */
public class OrdersCheckConst {
    public enum OrdersCheckEnum{
        /**
         * 已删除
         */
        DELETE(-1),
        /**
         * 新开
         */
        CREATE(1),
        /**
         * 已审查
         */
        CHECK(2);

        /**
         * 数据库数值
         */
        private final Integer value;

        private OrdersCheckEnum(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }
}
