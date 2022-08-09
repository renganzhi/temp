<template>
  <div :style="BodyStyle">
    <!-- <div :style="leftBox">
      <div :style="leftItem">
        <img src="./tx.jpeg" alt="" />
        <div :style="leftInfo">
          <p :style="leftNameInfo">张三</p>
          <p :style="leftPostInfo">区领导</p>
          <p :style="leftPhoneInfo">12345678910</p>
        </div>
      </div>
      <div :style="leftItem">
        <img src="./tx.jpeg" alt="" />
        <div :style="leftInfo">
          <p :style="leftNameInfo">张三</p>
          <p :style="leftPostInfo">区领导</p>
          <p :style="leftPhoneInfo">12345678910</p>
        </div>
      </div>
      <div :style="leftItem">
        <img src="./tx.jpeg" alt="" />
        <div :style="leftInfo">
          <p :style="leftNameInfo">张三</p>
          <p :style="leftPostInfo">区领导</p>
          <p :style="leftPhoneInfo">12345678910</p>
        </div>
      </div>
    </div> -->
    <div :style="rightBox">
        <div>
            <p :style="streetStyle">簇桥街道</p>
            <div :style="rightItemBox">
                <div :style="rightItem">
                    <img src="./tx.jpeg" alt="" />
                    <div :style="rightInfo">
                        <p :style="rightNameInfo">张三</p>
                        <p :style="rightPostInfo">区领导</p>
                        <p :style="rightPhoneInfo">12345678910</p>
                    </div>
                </div>
                <div :style="rightItem">
                    <img src="./tx.jpeg" alt="" />
                    <div :style="rightInfo">
                        <p :style="rightNameInfo">张三</p>
                        <p :style="rightPostInfo">区领导</p>
                        <p :style="rightPhoneInfo">12345678910</p>
                    </div>
                </div>
                <div :style="rightItem">
                    <img src="./tx.jpeg" alt="" />
                    <div :style="rightInfo">
                        <p :style="rightNameInfo">张三</p>
                        <p :style="rightPostInfo">区领导</p>
                        <p :style="rightPhoneInfo">12345678910</p>
                    </div>
                </div>
            </div>
        </div>
        <div>
            <p :style="streetStyle">金花街道</p>
            <div :style="rightItemBox">
                <div :style="rightItem">
                    <img src="./tx.jpeg" alt="" />
                    <div :style="rightInfo">
                        <p :style="rightNameInfo">张三</p>
                        <p :style="rightPostInfo">区领导</p>
                        <p :style="rightPhoneInfo">12345678910</p>
                    </div>
                </div>
                <div :style="rightItem">
                    <img src="./tx.jpeg" alt="" />
                    <div :style="rightInfo">
                        <p :style="rightNameInfo">张三</p>
                        <p :style="rightPostInfo">区领导</p>
                        <p :style="rightPhoneInfo">12345678910</p>
                    </div>
                </div>
                <div :style="rightItem">
                    <img src="./tx.jpeg" alt="" />
                    <div :style="rightInfo">
                        <p :style="rightNameInfo">张三</p>
                        <p :style="rightPostInfo">区领导</p>
                        <p :style="rightPhoneInfo">12345678910</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "CYZBTX",
  props: ["item"],
  data() {
    return {};
  },
  computed: {
    BodyStyle() {
      return {
        display:'flex',
        height: this.item.height + "px !important",
        width: this.item.width + "px !important",
      };
    },
    leftBox() {
      return {
        height: "100%",
        width: "500px",
        display: 'flex',
        "flex-direction": "column",
        "justify-content": "space-between",
      };
    },
    leftItem() {
      return {
        display: "flex",
        height: "32%",
        width: "100%",
        padding: "20px",
        background: 'pink'
      };
    },
    leftInfo() {
      return {
        display: 'flex',
        "flex-direction": "column",
        "align-items": "center",
        "justify-content": "center",
        'margin-left': "20px",
      };
    },
    leftNameInfo() {
      return {
        fontSize: 30 + "px",
        paddingTop: 10 + "px",
      };
    },
    leftPostInfo() {
      return {
        fontSize: 30 + "px",
        paddingTop: 10 + "px",
      };
    },
    leftPhoneInfo() {
      return {
        fontSize: 30 + "px",
        paddingTop: 10 + "px",
      };
    },
    rightBox() {
      return {
        flex: 1,
        background: '#bbb',
        'overflow-y': 'auto'
      };
    },
    streetStyle() {
      return {
        fontSize: 30 + 'px',
        color: '#ccc'
      };
    },
    rightItemBox() {
      return {
        overflow: 'hidden'
      };
    },
    rightItem() {
      return {
        float: 'left',
        display: "flex",
        height: "32%",
        width: "32%",
        padding: "20px",
        background: 'skyblue'
      };
    },
    rightInfo() {
      return {
        display: 'flex',
        "flex-direction": "column",
        "align-items": "center",
        "justify-content": "center",
        'margin-left': "20px",
      }; 
    },
    rightNameInfo() {
      return {
        fontSize: 30 + "px",
        paddingTop: 10 + "px",
      };
    },
    rightPostInfo() {
      return {
        fontSize: 30 + "px",
        paddingTop: 10 + "px",
      };
    },
    rightPhoneInfo() {
      return {
        fontSize: 30 + "px",
        paddingTop: 10 + "px",
      };
    },
  },
  watch: {},
  mounted() {},
  methods: {},
  beforeDestroy() {},
};
</script>
<style scoped lang="scss">
</style>
