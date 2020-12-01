import { gbs } from '@/config/settings'
import { newAjax } from '@/config/thirdLoginMix'
// 告警统计口径枚举
var alertStatisticsScopes = {
  1: '资源名称',
  2: '资源类型',
  3: '告警内容',
  4: '告警级别',
  5: '告警类型',
  6: '日志',
  7: '日志类型',
  8: '日志级别',
  9: '资源分组',
  10: '陷阱类型'
}

// 基本资源类型与名称的映射
var baseTypeMapName = {}
// 开启的告警级别
var activatedLevels = {}
// 告警级别值与级别名称的映射
var levelMapName = {}
// 告警级别值与色标的映射
var levelMapColor = {}
// 告警级别值与级别状态的映射
var levelMapStatus = {}
// 告警来源与名称的映射
var originMapName = {}
// 告警处理状态与名称的映射
var handleStatusMapName = {}
// 告警处理方式与名称的映射
var handleWayMapName = {}
// 告警通知方式与名称的映射
var notifyWayMapName = {
  email: '邮件',
  sms: '短信',
  socket: '消息中心',
  app: 'APP'
}
// 告警通知状态与名称的映射
var notifyStatusMapName = {
  WAITING: '待发送',
  SUCCESS: '发送成功',
  FAIL: '发送失败',
  SENDING: '发送中'
}
// 业务告警类型与名称的映射
var bAltMapName = {
  BASELINE: '基线告警',
  THRESHOLS: '阈值告警',
  SATISFACTION: '满意度告警'
}
// 业务告警类型与显示图标的映射
var bAltMapIcon = {
  BASELINE: 'fa fa-bars font-red-thunderbird',
  THRESHOLS: 'fa fa-exchange font-red-thunderbird',
  SATISFACTION: 'fa fa-heart-o font-red-thunderbird'
}

// $(function () {
// $.ajax({
//   dataType: 'json',
//   url: gbs.host + 'monitor/model/baseNeClass',
//   data: {},
//   type: 'get',
//   async: false,
//   error: function () {
//     //  tooltip('', '连接错误！', 'error')
//   },
//   success: function (datas) {
//     if (datas && datas.success) {
//       var baseTypes = datas.obj
//       for (var i = 0; i < baseTypes.length; i++) {
//         var baseType = baseTypes[i]
//         // 基本资源类型-名称
//         baseTypeMapName[baseType.value] = baseType.name
//       }
//     }
//   }
// })
newAjax({
  dataType: 'json',
  url: gbs.host + 'alert/currencyAlertmanager/enum',
  data: {},
  type: 'get',
  async: false,
  error: function () {
    // tooltip('', '连接错误！', 'error')
  },
  success: function (datas) {
    if (datas && datas.success) {
      var enums = datas.obj
      var levels = enums.level
      for (var i = 0; i < levels.length; i++) {
        var ele = levels[i]
        if (ele.status === 'ACTIVATED') {
          activatedLevels[ele.level] = ele.name
        }
        // 告警级别值-等级名称
        levelMapName[ele.level] = ele.name
        // 告警级别值-等级色标
        levelMapColor[ele.level] = ele.color.trim()
        // 告警级别值-等级状态
        levelMapStatus[ele.level] = ele.status
      }
      var origins = enums.origin
      for (var i = 0; i < origins.length; i++) {
        var origin = origins[i]
        // 告警来源-名称
        originMapName[origin.value] = origin.name
      }
      var statuss = enums.status
      for (var i = 0; i < statuss.length; i++) {
        var status = statuss[i]
        // 告警处理状态-名称
        handleStatusMapName[status.value] = status.name
      }
      var handleWays = enums.handleWay
      for (var i = 0; i < handleWays.length; i++) {
        var handleWay = handleWays[i]
        // 告警处理方式-名称
        handleWayMapName[handleWay.value] = handleWay.name
      }
    }
  }
})
// })
export default levelMapName
