// import api from '@/api/index'
// import store from '@/store/index'
import store from '@/vuex/store'
import { Notice } from 'view-design'
const trigger = {
  trigger: 'blur,change'
}

const formvalidation = {
  // 必填项
  required: {
    required: true,
    message: '必填项',
    // 这里存在去掉首尾的空格 会导致首尾存在空格时，长度校验异常 解决方案为将必填校验放在所有校验的最后 可解决这个问题 如 缺陷# 93450
    transform (value) {
      return typeof value !== 'undefined' && value !== null && String(value).trim()
    },
    ...trigger
  },
  selected: { required: true, type: 'array', min: 1, message: '必选项', ...trigger },
  number: {
    validator: (rule, value, callback) => {
      let newV = String(value).trim()
      if (rule.required && newV === '') {
        callback(new Error('必填项'))
      }

      if (newV === '' || /^(\-|\+)?\d+(\.\d+)?$/.test(newV)) {
        callback()
      } else {
        callback(new Error('必须是数字'))
      }
    }
  },
  // 浮点数
  floatNum: {
    validator: (rule, value, callback) => {
      if (value || value === 0) {
        let str = rule.decimalNum ? ('{1,' + rule.decimalNum + '}') : '+'
        let reg = new RegExp('^-?\\d+$|^(-?\\d+)(\\.\\d' + str + ')?$', 'g')
        if (reg.test(value)) {
          let str = value.toString().replace('-', '').split('.')[0]
          let nStr = Number(str).toString()
          if (nStr.length !== str.length) {
            callback(new Error('去掉整数无用的0'))
          } else {
            callback()
          }
        } else {
          callback(new Error(`填写正确的${rule.decimalNum ? ('保留小数点后' + rule.decimalNum) + '位的' : ''}浮点数`))
        }
      } else {
        callback()
      }
    }
  },
  range: {
    validator: (rule, value, callback) => {
      if (rule.type === 'string') {
        let len = value.length
        if (len >= rule.min && len <= rule.max) {
          callback()
        } else {
          callback(new Error(rule.message || `长度在${rule.min}到${rule.max}个字符之间`))
        }
      }

      if (rule.type === 'number') {
        if (value >= rule.min && value <= rule.max) {
          callback()
        } else {
          callback(new Error(rule.message || `数值在${rule.min}到${rule.max}之间`))
        }
      }
    }
  },
  inArray: {
    validator: (rule, value, callback) => {
      let arr = rule.data || [] // 数组
      let key = rule.key // 对象key
      let tip = `该${rule.tip}已存在`
      if (!key) { // [1,3,4]
        arr.includes(value) ? callback(new Error(tip)) : callback()
      } else { // [{a:1,b:2}]
        let flag = false
        arr.forEach(d => {
          if (d[key] === value) {
            flag = true
            return false
          }
        })
        flag ? callback(new Error(tip)) : callback()
      }
    }
  },
  // 最长大长度
  maxlength (mydata) {
    return {
      type: 'string',
      message: '最大长度为' + mydata + '个字符',
      max: mydata,
      ...trigger
    }
  },
  // 最小长度
  minlength (mydata) {
    return {
      type: 'string',
      message: '最小长度为' + mydata + '个字符',
      min: mydata,
      ...trigger
    }
  },
  // input最大长度为50个字符
  textMaxlen: {
    type: 'string',
    message: '最大长度为50个字符',
    max: 50,
    ...trigger
  },
  // 描述、备注等textarea最大长度为255个字符
  textareaMaxlen: {
    type: 'string',
    message: '最大长度为255个字符',
    max: 255,
    ...trigger
  },
  // // 电话号码-段位验证
  // telephone: {
  //   pattern: /(^0?1[3|4|5|6|7|8|9][0-9]\d{8}$)|(^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8}))$)/,
  //   message: '手机号格式不正确，请重新输入',
  //   ...trigger
  // },
  // 电话号码-非段位验证
  telephone: {
    pattern: /(^0?1[0-9]\d{9}$)/,
    message: '手机号格式不正确，请重新输入',
    ...trigger
  },
  // 电话号码-带中国区号
  isMobileNumber: {
    pattern: /^((\+?86))?1[0-9]\d{9}$/,
    message: '手机号格式不正确，请重新输入',
    ...trigger
  },
  // 非负整数验证
  integer: {
    pattern: /^([1-9]\d*|[0]{1,1})$/,
    message: '只能输入非负整数',
    ...trigger
  },

  // 正整数验证
  zIndex: {
    pattern: /^[0]*[1-9]\d*$/,
    message: '请输入正整数',
    ...trigger
  },

  // 百分比验证(0-100最多两位小数)
  percent: {
    pattern: /(^(\d|[1-9]\d)(\.\d{1,2})?$)|(^100$)/,
    message: '只能输入0-100数字且最多保留2位小数'
  },
  // 字符验证
  stringCheck: {
    pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]+$/,
    message: "支持中英文、数字和'_'的组合"
  },
  alphaBegin: {
    pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/g,
    message: "以字母开头，字母、数字或'_'组成"
  },
  // 英文验证
  alpha: {
    pattern: /^[a-zA-Z]+$/,
    message: '只能包括英文字母'
  },
  // 英文与数字验证
  alphaNum: {
    pattern: /^[a-zA-Z0-9]+$/,
    message: '只能输入包括数字、字母'
  },
  // 英文与数字特殊格式验证
  alphaOrNum: {
    pattern: /^([a-zA-Z]|_(?!_))+([a-zA-Z\d]|_(?!_))+$/,
    message: '以字母开头，字母和数字组成'
  },
  // 邮箱验证
  email: {
    // type: 'email',
    pattern: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z]{2,4})+$/,
    message: '邮箱格式不正确，请重新输入',
    ...trigger
  },
  // ip地址验证
  ip: {
    pattern: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
    message: '请正确输入ip地址',
    ...trigger
  },
  // mac地址校验
  mac: {
    pattern: /^([0-9a-fA-F]{2})(((-[0-9a-fA-F]{2}){5}$)|((:[0-9a-fA-F]{2}){5}$))/,
    message: '请输入正确的mac地址',
    ...trigger
  },
  // 网址验证
  url: {
    // pattern: /^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/i,
    pattern: '^((https|http|ftp|rtsp|mms){1}://){1}' +
    "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" + // ftp的user@
    '(([0-9]{1,3}\\.){3}[0-9]{1,3}' + // IP形式的URL- 199.194.52.184
    '|' + // 允许IP和DOMAIN（域名）
    "([0-9a-z_!~*'()-]+\\.)*?" + // 域名- www.
    '([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\.' + // 二级域名
    '[a-z]{2,6})' + // first level domain- .com or .museum
    '(:([0-9]|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5]))?' + // 端口- :80
    '((/?)|' + // a slash isn't required if there is no file name
    "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$",
    message: '请输入正确的的URL',
    ...trigger
  },
  port: {
    validator: (rule, value, callback) => {
      if (value && !(/^[0]*[1-9]\d*$/.test(value) && value * 1 >= 0 && value * 1 <= 65535)) {
        callback(new Error('请输入[1-65535]之间的整数'))
      } else {
        callback()
      }
    }
  },
  timeout: {
    pattern: /^[1-5]$/,
    message: '请输入1-5之间的整数',
    ...trigger
  },
  // 不为undefined
  notUndefined: {
    validator: (rule, value, callback) => {
      if (value.toUpperCase() === 'UNDEFINED') {
        callback(new Error(`必填项`))
      } else {
        callback()
      }
    }
  },
  // 不为null
  notNull: {
    validator: (rule, value, callback) => {
      if (value.toUpperCase() === 'NULL') {
        callback(new Error(`不能为null`))
      } else {
        callback()
      }
    }
  },
  // 多选框最大选择长度校验
  checkLength (length, msg) {
    msg = msg || '最多可以选择' + length + '项数据'
    return {
      trigger: 'change',
      asyncValidator: (rule, value, callback) => {
        if (value) {
          if (value.length > length) {
            callback(new Error(msg))
          } else {
            callback()
          }
        } else {
          callback()
        }
      }
    }
  },
  // 名字重复校验
  // checkExist (url, id, tip) {
  //   tip = '该' + (tip || '名称') + '已存在'
  //   return {
  //     trigger: 'blur',
  //     asyncValidator: (rule, value, callback) => {
  //       if (value) {
  //         store.commit('base/setLoading', false)
  //         api[url]({ name: value, id: id }).then(res => {
  //           store.commit('base/setLoading', true)
  //           if (typeof res.success === 'undefined') {
  //             res ? callback(new Error(tip)) : callback()
  //           }
  //           if (res.success) {
  //             if (res.obj === false) {
  //               callback(new Error(tip))
  //             } else {
  //               callback()
  //             }
  //           } else {
  //             callback(new Error(tip))
  //           }
  //         }, errRes => {
  //           callback(new Error('名称校验请求失败'))
  //         })
  //       } else {
  //         callback()
  //       }
  //     }
  //   }
  // },
  // checkExistTwo (url, id, tip) {
  //   tip = '该' + (tip || '名称') + '已存在'
  //   return {
  //     trigger: 'blur',
  //     asyncValidator: (rule, value, callback) => {
  //       if (value) {
  //         store.commit('base/setLoading', false)
  //         api[url]({ name: value, id: id }).then(res => {
  //           store.commit('base/setLoading', true)
  //           if (res.success) {
  //             if (res.obj === true) {
  //               callback(new Error(tip))
  //             } else {
  //               callback()
  //             }
  //           }
  //         }, errRes => {
  //           callback(new Error('名称校验请求失败'))
  //         })
  //       } else {
  //         callback()
  //       }
  //     }
  //   }
  // },
  // 重名校验
  // pram为接口需要的参数{}
  // pram中的其他请求参数在使用时需要使用watch触发验证,验证的参数名需要传nameKey
  // example
  // watch: {
  //   'addForm.type': function (v) {
  //     this.rules.name = [this.$rules.chkEsxist('ptTemChkName', { id: this.$route.params.id,nameKey: 'name', type: v })]
  //   }
  // },
  // rules = {
  //   name: [this.$rules.chkEsxist('ptTemChkName', { id: this.$route.params.id,nameKey: 'name', type: this.addForm.name })],
  // }
  // chkExist (url, pram, tip) {
  //   tip = '该' + (tip || '名称') + '已存在'
  //   return {
  //     trigger: 'blur',
  //     asyncValidator: (rule, value, callback) => {
  //       if (value) {
  //         store.commit('base/setLoading', false)
  //         api[url]({ ...pram, [pram.nameKey || 'name']: value, nameKey: undefined }).then(res => {
  //           store.commit('base/setLoading', true)
  //           if (res.obj === true) {
  //             callback(new Error(tip))
  //           } else {
  //             callback()
  //           }
  //         }, errRes => {
  //           callback(new Error('名称校验请求失败'))
  //         })
  //       } else {
  //         callback()
  //       }
  //     }
  //   }
  // },

  //   密码验证
  passwordCheck: {
    pattern: /^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{6,100}$/,
    message: '必须包含大写字母、小写字母和数字'
  },

  // 根据用户密码配置验证密码
  passwordCheckByConf (pram) {
    let tip = '密码必须包含'
    let reg = ''
    let regObj = {
      upperCase: {
        reg: '(?=.*[A-Z].*)',
        tip: '大写字母'
      },
      lowerCase: {
        reg: '(?=.*[a-z].*)',
        tip: '小写字母'
      },
      pwdNumber: {
        reg: '(?=.*[0-9].*)',
        tip: '数字'
      }
    }
    for (let key in pram) {
      if (pram[key]) {
        reg += regObj[key].reg
        tip += regObj[key].tip + '、'
      }
    }
    reg = new RegExp(reg)
    tip = tip.substr(0, tip.length - 1)
    return {
      trigger: 'blur,change',
      validator: (rule, value, callback) => {
        if (!reg.test(value)) {
          callback(new Error(tip))
        } else {
          callback()
        }
      }
    }
  },

  // 时间验证
  // timeFormat: {
  //   pattern: /^(?:1[0-9]|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$/,
  //   message: "请正确输入时间格式"
  // },

  // oid: {
  //   pattern: /^([0-9A-Za-z]+)(.[0-9A-Za-z]+)+$/,
  //   message: "请输入正确的OID格式"
  // },

  // 文件类型验证
  fileType: {
    validator: (rule, value, callback) => {
      var filepath = value.substring(value.lastIndexOf('.') + 1, value.length)
      if (filepath === 'jpg' || filepath === 'png') {
        callback()
      } else {
        callback(new Error('只能上传JPG或PNG格式的图片'))
      }
    }
  },

  // 不能含有特殊字符
  noSpecialChar: {
    validator: (rule, value, callback) => {
      var str = new RegExp("[`~!@#$^&*()=|{}';',<>》《?~！@#￥……&*（）——|{}【】‘；”“'。，、？%+_-]")
      if (((!str.test(value))) && !/\s/.test(value)) {
        callback()
      } else {
        callback(new Error('不能含有特殊字符'))
      }
    }
  },

  // 阈值校验
  rangeValid (list, fn, index, infinite, max) {
    return {
      trigger: 'blur',
      asyncValidator: (rule, value, callback) => {
        let obj = list[index]
        let field = rule.field
        let ruleForm = fn(index)
        let ruleForms = fn()
        let begin = parseFloat(obj.begin)
        let end = parseFloat(obj.end)
        ruleForms.forEach(d => {
          if (!d.rangeObj) {
            d.rangeObj = { begin: 'pass', end: 'pass' }
            d.checkRangeResult = { begin: false, end: false, result: 'noCheck' }
          }
        })
        if (ruleForm.checkRangeResult[field]) {
          ruleForm.checkRangeResult[field] = false
          ruleForm.checkRangeResult.result === 'fail' ? callback(new Error('阈值区间重合')) : callback()
        } else {
          if (!infinite && !begin && !end && begin !== 0 && end !== 0) {
            callback(new Error('必填项'))
            ruleForm.rangeObj[field] = 'fillOne'
            this.checkRangeAgain(ruleForm, ruleForms, 'noCheck')
          } else if ((value || value === 0) && !(/^(\-|\+)?\d+(\.\d+)?$/.test(value))) {
            callback(new Error('只能输入数字类型'))
            ruleForm.rangeObj[field] = 'number'
            this.checkRangeAgain(ruleForm, ruleForms, 'noCheck')
          } else if (max && value > max) {
            callback(new Error(`最大值为${max}`))
            ruleForm.rangeObj[field] = 'max'
            this.checkRangeAgain(ruleForm, ruleForms, 'noCheck')
          } else if ((begin || begin === 0) && (end || end === 0) && begin >= end) {
            callback(new Error('开始值应小于结束值'))
            ruleForm.rangeObj[field] = 'compare'
            this.checkRangeAgain(ruleForm, ruleForms, 'noCheck')
          } else {
            // 阈值校验
            ruleForm.rangeObj[field] = 'pass'
            let otherField = field === 'end' ? 'begin' : 'end'
            // 若另外错误状态为compare和fillOne时，重新校验
            if (ruleForm.rangeObj[otherField] === 'compare' || ruleForm.rangeObj[otherField] === 'fillOne') {
              let vNode = ruleForm.validateField ? ruleForm : ruleForm.$children[0]
              vNode.validateField(otherField)
            }
            let result = this.checkRange(list, ruleForms)
            this.checkRangeAgain(ruleForm, ruleForms, result)
            result === 'fail' ? callback(new Error('阈值区间重合')) : callback()
          }
        }
      }
    }
  },
  checkRangeAgain (ruleForm, ruleForms, result) {
    if (result === 'fail' || ruleForm.checkRangeResult.result === 'fail') {
      ruleForms.forEach(item => {
        item.checkRangeResult.result = result
        let vNode = item.validateField ? item : item.$children[0]
        if (item.rangeObj.begin === 'pass') {
          item.checkRangeResult.begin = true
          vNode.validateField('begin')
        }
        if (item.rangeObj.end === 'pass') {
          item.checkRangeResult.end = true
          vNode.validateField('end')
        }
      })
    }
  },
  checkRange (d, ruleForms) {
    // begin和end存在错误时不进行阈值校验
    for (let item of ruleForms) {
      if (item.rangeObj.begin !== 'pass' || item.rangeObj.end !== 'pass') return 'noCheck'
    }
    // 阈值校验
    let begin = []
    let end = []
    for (let i = 0; i < d.length; i++) {
      let tr1 = d[i].begin
      let tr2 = d[i].end
      if (!tr1 && !tr2 && d.length >= 2) {
        return 'fail'
      }
      begin.push(tr1 === '' && tr2 !== '' ? Number.NEGATIVE_INFINITY : parseFloat(tr1))
      end.push(tr2 === '' && tr1 !== '' ? Number.POSITIVE_INFINITY : parseFloat(tr2))
    }
    if (d.length >= 2) {
      for (let i = 0; i < begin.length; i++) {
        for (let j = i + 1; j < begin.length; j++) {
          if (Math.max(begin[i], begin[j]) < Math.min(end[i], end[j])) {
            return 'fail'
          }
        }
      }
    }
    return 'pass'
  },
  checkSearch (arg) {
    if (!arg) return true
    if (Object.prototype.toString.call(arg) === '[object Object]') arg = Object.values(arg) || []
    if (!Array.isArray(arg)) arg = [arg]
    let val = arg.every(item => {
      if (item && item.length > 50) {
        Notice.info({
          desc: '搜索框最多为50个字符'
        })
        return false
      }
      return true
    })
    return val
  },
  treeLevelValid (chkedLevel, id, pId, level, tip) {
    return {
      trigger: 'change',
      asyncValidator: (rule, value, callback) => {
        if ((value || value === 0) && chkedLevel && !chkedLevel(id, pId, level)) {
          callback(new Error(`${tip}最多只能添加${level}级!`))
        }
        callback()
      }
    }
  }
}

export default formvalidation
