<template>
    <div>
        <section v-for="(section, sectionIndex) in ['lengend', 'chart', 'axis']" :key="`section${sectionIndex}`">
                <div class="m-gap form-group" v-show="configOptions[section] && configOptions[section].length > 0">{{sectionMap[section]}}</div>
                <div
                    class="form-group cols2"
                    v-for="(item, index) in configOptions[section]"
                    :key="`axis_${index}`"
                >
                    <label>{{ item.name }}</label>
                    <div class="color-w200">
                        <template v-if="item.tag == 'select'">
                            <select :value="configItems[item.key]" @change="change(item.key, 'select', $event)">
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
                        <template v-else-if="item.tag == 'singleColor'">
                            <Vcolor :data="configItems[item.key]"
                                :key="11"
                                type="bgClr"
                                @getdata="change(item.key, 'singleColor', $event)"></Vcolor>
                        </template>
                    </div>
                </div>
        </section>
    </div>
</template>

<script>
// import { axis } from "@/components/Common/EditComp/Vchart/config";


// console.log(axis);

import Vcolor from '@/components/Common/Vcolor'

export default {
    name: "chartStyle",
    props: ["configItems"],
    components: {Vcolor},
    data() {
        return {
            sectionMap: {
                lengend: '图例配置',
                chart: '图表样式',
                axis: '坐标轴样式',
            },
            configOptions: {},
            // lengend: [],
            // axis: axis,
            // chart: []
        };
    },
    computed: {
        curType() {
            return this.configItems.chartType;
        },
        chart () {
            return this.configItems.chart || []
        },
        lengend () {
            return this.configItems.lengend || []
        },
        axis () {
            return this.configItems.axis || []
        },
    },
    mounted () {
        this.initOptions(this.curType)
    },
    methods: {
        initOptions (type) {
            if (type.indexOf('ve-') > -1) {
                this.configOptions = require(`@/components/Common/EditComp/Vchart/config`).default.styles
            } else {
                this.configOptions = require(`@/components/Common/EditComp/${type}/config`).default.styles
            }
        },
        change(key, tag, event) {
            // console.log(event.target.value);
            if (tag == 'singleColor') {
                const value = event.color;
                this.$emit("change", key, value);
            } else {
                const value = event.target.value
                // console.log();
                this.$emit("change", key, value);
            }
        }
    },
    watch: {
        // 'item.ctLegendShow' (newVal, oldVal) {
        //     this.$emit('change', 'ctLegendShow', newVal);
        // }
        'configItems.chartType' (newVal, oldVal) {
            this.initOptions(newVal)
            console.log('type', newVal, this.configOptions)
        }
    }
};
</script>

<style scoped></style>
