<template>
    <div class="range_color_box"
        @click="reverseColor"
        :style="{'background': 'linear-gradient(45deg, ' + colors[0]  +',' + colors[1] + ')'}">
        <div class="color-w15">
        <Vcolor :data="colors[0]"
                :index="0"
                @getdata="getColorStart"></Vcolor>
        </div>
        <div class="color-w15"
            style="float: right">
        <Vcolor :data="colors[1]"
                :index="1"
                @getdata="getColorEnd"></Vcolor>
        </div>
    </div>
</template>

<script>
import Vcolor from '@/components/Common/Vcolor'

export default {
    name: "RangeColor",
    components: {Vcolor},
    props: {
        data: {
            type: Array,
            default () {
                return ['rgba(0, 0, 0, 1)', 'rgb(255, 255, 255, 1)']
            }
        }
    },
    data () {
        return {
            colors: this.data
        }
    },
    methods: {
        reverseColor () {
            this.colors.reverse();
            this.emit()
        },
        getColorStart( e) {
            this.colors[0] = e.color
            this.emit()
        },
        getColorEnd( e) {
            this.colors[1] = e.color
            this.emit()
        },
        emit() {
            this.$emit('getdata', {
                color: this.colors,
                // type: this.type,
                // index: this.index
            })
        }
    }
}
</script>

<style scoped>
.range_color_box {
    width: 100%;
    height: 10px;
    float: left;
    /* margin-left: 10px; */
    margin-top: 3px;
    /* margin-right: 5px; */
    cursor: pointer;
}
</style>
