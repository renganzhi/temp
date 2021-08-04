<template>
  <div id="app">
    <!-- 等待蒙板层 -->
    <!-- 这里关于screen及其css最好在打包时注释掉，simo里已经存在 -->
    <div id="lead-screen">
      <div id="loading">
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
      </div>
    </div>
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  data: function () {
    return {
      bodyClass: ''
    }
  },
  created: function () {
    $('html').attr('data-theme', 'default')
    var _hash = window.location.hash
    if (_hash.split('/')[1]) {
      var _url =
        window.location.protocol + '//' + window.location.host + '/index'
      if (process.env.NODE_ENV != 'development') {
        window.location.href = _url
      }
    }
  }
}
</script>

<style lang="scss">
@import "main.scss";

html,
body {
  width: 100%;
  height: 100%;
  margin: 0px;
  overflow: hidden;
}
.sp-replacer {
  width: 100% !important;
  display: flex;
}
.sp-preview {
  flex: 1;
}
.disShow {
  display: none !important;
}
.select2-container.select2-container--default {
  z-index: 10100;
}
#lead-screen {
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0px;
  bottom: 0;
  z-index: 20100;
  background: rgba(0, 0, 0, 0.5);
  display: none;
}
#app {
  width: 100%;
  height: 100%;
  /* 指标显示器字体，时间器字体 */
}
/* 框选区域样式 */
#bottom {
  position: absolute;
  bottom: 0px;
  width: 100%;
  height: 40px;
  border: 1px solid #000;
  background: #000;
  color: #fff;
}
.tempDiv {
  border: 1px dashed blue;
  background: #5a72f8;
  position: absolute;
  z-index: 999999;
  /* filter:alpha(opacity: 10); */
  opacity: 0.3;
}
#toast-container{
  color: white;
  button{
    background-color: transparent !important;
  }
}
.modal-dialog-centered{
  .row{
    width: 100%;
    justify-content: center;
    margin: 0;
  }
}

/* .modal-backdrop {
  z-index: 20099;
} */

/* 气泡图 */
.clear_float:after {
  display: block;
  clear: both;
  content: "";
}
.text_overflow {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.modal.in .modal-dialog {
  position: relative;
  margin: 0 auto !important;
  top: 50%;
  transform: translateY(-50%);
}
#toast-container{
  .toast-close-button{
    background-color: transparent;
  }
}
.simoLink:hover {
    color: #0253f5 !important;;
    text-decoration: underline;
}

html[data-theme="default"] {
  option,
  optgroup {
    background: #0a0e1a !important;
    color: #e5e5e5 !important;
  }

  #menu_1 .nav-item:hover,
  #menu_1 .nav-item.active {
    background-color: #1c2132;
  }

  /*
校验
*/
  .required {
    color: #ff0000;
  }

  label.error {
    color: #ff0000;
  }

  select.error + .select2-container .select2-selection {
    border: 1px solid #dd1b1b !important;
  }

  button,
  .ux-btn,
  .btn {
    color: #cad6dd;
    background: #0088cc;
  }
  button[disabled],
  button[disabled]:hover,
  .ux-btn[disabled],
  .ux-btn[disabled]:hover,
  .btn[disabled],
  .btn[disabled]:hover {
    background-color: #525766;
  }

  button:hover,
  .ux-btn:hover,
  .btn.focus,
  .btn:hover,
  .btn-group .btn-default.active:hover {
    color: #cad6dd;
    background-color: #02699c;
  }
  .modal-header .close {
    background-color: transparent;
  }

  /*
表格
*/
  .table {
    color: #cad6dd;
  }
  .table thead {
    background: #1c2132;
  }
  .table th {
    border-right: 1px solid #1b2031;
    background: #1c2132;
  }
  .table td {
    border-right: 1px solid #22283a;
  }
  .table tbody tr:nth-of-type(2n + 1) {
    background-color: transparent !important;
  }
  .table tbody tr:nth-of-type(2n) {
    background-color: #1f2434 !important;
  }
  .table tbody tr:hover {
    background-color: #303852 !important;
  }

  .table .table {
    background: #222739;
  }

  .table tbody tr.active,
  .fixed-table-container tbody .selected td {
    background-color: #242c4a !important;
  }

  input:not([type="checkbox"]):not([type="radio"]),
  textarea,
  select {
    border: solid 1px #3d445a;
    background-color: transparent !important;
    color: #cad6dd !important;
  }
  .select2-selection {
    border: solid 1px #3d445a;
    background-color: transparent !important;
    color: #cad6dd !important;
  }

  .select2-container.select2-container--focus .select2-selection {
    border: solid 1px #0088cc !important;
  }

  .select2-container--default
    .select2-selection--multiple
    .select2-selection__choice {
    background-color: #323746;
    color: #cad6dd;
  }

  .select2-container--default .select2-results__option[aria-selected="true"] {
    background-color: #222938;
  }

  .select2-container--default
    .select2-results__option--highlighted[aria-selected] {
    background-color: transparent;
  }

  .select2-dropdown {
    border: 1px solid #3d445a;
    background-color: #0a0e1a;
    color: #b2b2b2;
  }
}
/*
    select2
*/
html[data-theme="blackWhite"],
html[data-theme="blueWhite"] {

  option,
  optgroup {
    background: #ffffff!important;
    color: #666666 ;
  }
  .dropdown-menu{
    background-color: #fff !important;
    color: #50607c !important;
  }

  #menu_1 .nav-link:hover {
    background-color: transparent !important;
  }

  #menu_1 .nav-item:hover,
  #menu_1 .nav-item.active {
    background-color: #ffffff;
  }

  #menu_1 .menu-span {
    color: #3e4554;
  }

  /*
校验
*/
  .required {
    color: #f32d5c;
  }

  label.error {
    color: #f32d5c;
  }

  select.error + .select2-container .select2-selection {
    border: 1px solid #f32d5c !important;
  }

  button,
  .ux-btn,
  .btn {
    color: #026bf4;
    background: #ffffff;
    border: solid 1px;
  }
  button[disabled],
  button[disabled]:hover,
  .ux-btn[disabled],
  .ux-btn[disabled]:hover,
  .btn[disabled],
  .btn[disabled]:hover {
    background: #ebedf1;
    border: solid 1px #569bf4;
    color: #adb9ca;
  }

  button:hover,
  .ux-btn:hover,
  .btn.focus,
  .btn:hover,
  .btn-group .btn-default.active:hover {
    color: #026bf4;
    background-color: #ebedf1;
  }

  .btn:focus {
    color: #026bf4;
  }

  input:not([type="checkbox"]):not([type="radio"]),
  textarea,
  select {
    border: solid 1px #adb9ca !important;
    background-color: #ffffff !important;
    color: #50607c !important;
  }

  /*
表格
*/
  .table {
    color: #50607c;
  }
  .table tr {
    border-bottom: solid 1px #ebedf1 !important;
  }
  .table thead {
    background: #ebedf1;
  }
  .table th {
    background: #ebedf1;
  }
  .table tbody tr{
    background-color: transparent !important;
  }
  .table tbody tr:hover {
    background-color: #ffffff !important;
    box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.1);
  }

  .table .table {
    background: #ffffff;
  }

  .table tbody tr.active,
  .fixed-table-container tbody .selected td {
    background-color: #ffffff !important;
  }

  /*
    select2
*/
  .select2-selection {
    border: solid 1px #adb9ca !important;
    background-color: #ffffff !important;
    color: inherit !important;
  }

  .select2-container.select2-container--focus .select2-selection {
    border: solid 1px #026bf4 !important;
  }

  .select2-container .select2-selection__placeholder {
    color: #8a94a4 !important;
  }

  .select2-selection__rendered {
    color: #50607c;
  }

  .select2-container--default
    .select2-selection--multiple
    .select2-selection__choice {
    background-color: #adb9ca;
    color: #ffffff;
  }
  .select2-container--default
    .select2-selection--multiple
    .select2-selection__choice:hover {
    background-color: #8a94a4;
  }

  .select2-container--default
    .select2-selection--multiple
    .select2-selection__choice__remove {
    color: #ffffff;
  }

  .select2-container--default .select2-results__option[aria-selected="true"] {
    background-color: #adb9ca;
    color: #ffffff;
  }

  .select2-container--default
    .select2-results__option--highlighted[aria-selected] {
    background-color: #ebedf1;
    color: #50607c;
  }

  .select2-dropdown {
    border: 1px solid #adb9ca;
    background-color: #ffffff;
    color: #50607c;
  }

  /***
modal
***/
  .modal-content {
    background: #ffffff;
    color: #3e4554;
  }

  .modal-header {
    background-color: #ebedf1;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  }
  .modal-header .close {
    background-color: transparent;
    color: #8a94a4;
    opacity: 1;
  }
  .modal-header .close:hover {
    background-color: transparent !important;
  }

  .modal-title {
    color: #50607c;
  }
  .modal-title:before {
    background: #026bf4;
  }

  .modal-body {
    color: #3e4554;
  }

  .modal-footer button {
    background: #026bf4;
    color: #ffffff;
    border: none;
  }
  .modal-footer button:hover {
    background: #0253f5;
    color: #ffffff;
    border: none;
  }

  .modal-footer button[data-dismiss="modal"]:last-child {
    color: #026bf4;
    background: #ffffff;
    border: solid 1px;
  }
  .modal-footer button[data-dismiss="modal"]:last-child:hover {
    color: #026bf4;
    background-color: #ebedf1;
  }

  .moniModal.modal .modal-header {
    background: transparent;
    border-bottom: 1px solid #ebedf1;
  }

  .moniModal.modal button.close {
    color: #026bf4;
  }

  .moniModal .modal-content {
    background: #f8f8f8;
  }

  .moniModal .pre-page {
    color: #8a94a4;
  }
  .moniModal .pre-page:hover {
    color: #026bf4;
  }

  .moniModal .now-page {
    color: #3e4554;
  }

  #smallImg {
    background: #02112d;
  }

  #menu_left .nav-item.open .sub-menu li:hover {
    background: #041c4c;
  }

  #menu_left > li.nav-item > a:hover {
    background: #041c4c;
  }
}
</style>
