<template>
    <div>
        <!-- <template v-show="lengend.length > 0">
            <div class="m-gap form-group">图例配置</div>
            <div class="form-group cols2">
                <label>图例可见性</label>
                <select v-model="item.ctLegendShow" @change="change">
                    <option value="true">显示</option>
                    <option value="false">隐藏</option>
                </select>
            </div>
        </template> -->
        <template v-show="axis.length > 0">
            <div class="m-gap form-group">坐标轴配置</div>
            <div
                class="form-group cols2"
                v-for="(item, index) in axis"
                :key="`axis_${index}`"
            >
                <label>{{ item.name }}</label>
                <template v-if="item.tag == 'select'">
                    <select :value="configItems[item.key]" @change="change(item.key, $event)">
                        <option
                            v-for="(option, optIndex) in item.options"
                            :key="`${option.name}_${optIndex}`"
                            :value="option.value"
                            >{{ option.name }}</option
                        >
                    </select>
                </template>
                <!-- <template v-else-if="item.tag == 'input'">
                    <input
                        class="w-90"
                        :type="item.type"
                        v-model="configItems[item.key]"
                    />
                    {{ item.unit || "" }}
                </template> -->
            </div>
        </template>
        <!-- <template v-show="chart.length > 0">
            <div class="m-gap form-group">图表样式</div>
            <div class="form-group cols2">
                <label>图例可见性</label>
                <select v-model="item.ctLegendShow" @change="change">
                    <option value="true">显示</option>
                    <option value="false">隐藏</option>
                </select>
            </div>
        </template> -->
    </div>
</template>

<script>
import { axis } from "@/components/Common/EditComp/Vchart/config";

console.log(axis);
export default {
    name: "chartStyle",
    props: ["configItems"],
    data() {
        return {
            lengend: [],
            axis: axis,
            chart: []
        };
    },
    computed: {
        curType() {
            return this.item.chartType;
        }
    },
    // mounted () {
    //     if (curType)
    // },
    methods: {
        change(key, event) {
            // console.log(event.target.value);
            const value = event.target.value
            // console.log();
            this.$emit("change", key, value);
        }
    },
    watch: {
        // 'item.ctLegendShow' (newVal, oldVal) {
        //     this.$emit('change', 'ctLegendShow', newVal);
        // }
        // 'item.chartType' (newVal, oldVal) {
        //     this.axis =
        // }
    }
};
</script>

<style scoped></style>
