export const defaultPsw = {
  'formation': { 'upperCase': true, 'lowerCase': true, 'pwdNumber': true },
  'length': { 'minLength': 8, 'maxLength': 20 }
}

export const defaultLogo = {
  title: '一体化运维管理系统',
  version: 'V4.0.1'
}

export const notifyWays = { // 消息推送方式
  MSG: '消息中心',
  MAIL: '邮箱',
  SMS: '短信',
  APP: 'APP',
  MP: '公众号',
  WEWORK: '企业微信'
}

export const interfaceText = {
  'up': '正常',
  'down': '故障',
  'testing': '测试',
  'unknown': '未知',
  'dormant': '休眠',
  'notPresent': '硬件缺失',
  'lowerLayerDown': '底层不可用',
  'notUsed': '未使用'
}
