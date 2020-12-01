/*
import {mapActions, mapGetters } from 'vuex'
import qs from 'qs'
export default {
  computed: {
    ...mapGetters([
      'thirdUser'
    ])
  },
  methods:{
    ...mapActions([
      'changeThirdUser'
    ]),
    async checkLogin(ipPort){
      let userRes = {success:true}
      if(!this.thirdUser){
        userRes = await this.axios.get(`/mc/encodeUserInfo`) //获取第三方用户信息
        this.changeThirdUser(userRes.msg)
      }
      //第三方用户登录
      let loginRes = await this.axios.post(`http://${ipPort}/sso/token`, qs.stringify({data: this.thirdUser}), { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } })
      return userRes && loginRes && userRes.success && loginRes.success
    }
  }
}
*/

import store from '@/vuex/store'
import Vue from 'vue'
import qs from 'qs'
import { gbs } from '@/config/settings'
export const checkLogin = async function(ipPort) {
  let userRes = {success:true}
  if(!store.state.thirdUser){
    userRes = await Vue.axios.get(`/mc/encodeUserInfo`) //获取第三方用户信息
    store.dispatch("changeThirdUser",userRes.msg)
  }
  //第三方用户登录
  let loginRes = await Vue.axios.post(`http://${ipPort}/sso/token`, qs.stringify({data: store.state.thirdUser}), { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } })
  return userRes && loginRes && userRes.success && loginRes.success
}

export const newAjax = function(opt){
  // let thirdConf = store.state.thirdConf
  let url = handleUrl(opt.url)
  // if(thirdConf.isThird){
  //   let host = gbs.host
  //   if(host && url.indexOf(host) !== -1){
  //     url = opt.url.split(host)[1]
  //   }
  //   console.log(url)
  //   if(url.indexOf('_token_u_=token') !== -1){
  //     url = thirdConf.curDataHost + url
  //   }else{
  //     url = thirdConf.curDataHost + url + (url.indexOf('?') === -1 ? '?' : '&') + '_token_u_=token'
  //   }
  // }
  toAjax({...opt,url},1)
}

function handleUrl (url){
  let thirdConf = store.state.thirdConf
  if(thirdConf.isThird){
    let host = gbs.host
    if(host && url.indexOf(host) !== -1){
      url = url.split(host)[1]
    }
    console.log(url)
    if(url.indexOf('_token_u_=token') !== -1){
      url = thirdConf.curDataHost + url
    }else{
      url = thirdConf.curDataHost + url + (url.indexOf('?') === -1 ? '?' : '&') + '_token_u_=token'
    }
  }
  return url
}

function toAjax (opt,i){
  $.ajax({
    ...opt,
    error: async function(xhr){
      let thirdConf = store.state.thirdConf
      if(xhr.status === 776){
        if(i===1){
          await checkLogin(thirdConf.thirdIpPort) && toAjax(opt,2)
        }
      }else if(typeof opt.errorCallback === "function"){
        opt.errorCallback(xhr) 
      }
    }
  })
}

export const getTopoIcon = function(opt){
  let url = handleUrl(opt.url)
  var ajax = new XMLHttpRequest();
  ajax.open('get', url);
  ajax.responseType = 'blob'
  ajax.onload = function (e) {
    var reader =  new window.FileReader();
    reader.readAsDataURL(ajax.response); 
    reader.onloadend = function() {
      opt.callback(opt.curThis,reader.result)
    }
  }
  ajax.send();
  ajax.onreadystatechange = async function (){
    if (ajax.status == 776){
      await checkLogin(thirdConf.thirdIpPort) && getTopoIcon(opt)
    }
  }
}