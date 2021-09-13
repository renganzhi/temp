import Vue from 'vue'
import UsModal from './main.vue'

const ensureConstructor = Vue.extend(UsModal)

UsModal.confirm = function (obj, fn, cancelFn, noFn) {
  // eslint-disable-next-line new-cap
  let instance = new ensureConstructor().$mount()
  if (Object.prototype.toString.call(obj) === '[object Object]') {
    Object.assign(instance, obj)
  } else {
    instance.content = obj
  }
  instance.onEnsure = fn
  instance.onClose = cancelFn
  instance.onNo = noFn
  instance.remove = function () {
    document.body.removeChild(instance.$el)
    instance = null
  }
  document.body.appendChild(instance.$el)
  Vue.nextTick(() => {
    instance.isOpen = true
  })
}
export default UsModal
