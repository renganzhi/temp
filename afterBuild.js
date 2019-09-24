var fs = require('fs')
var path = require('path')

var fileName = 'index.html'
var targetName = 'main.html'
var sourceFile = path.join(__dirname, 'dist', fileName)
var destPath = path.join(__dirname, 'dist', targetName)
var data = ''
var arguments = process.argv.splice(2)
var replaceText = arguments[0] || '/leaderview-static/' // 目标目录

// var ignoreJs = ['jquery', 'jquery-ui']
var ignoreJs1 = ['jquery', 'jquery-ui']
var ignoreJs = ['jquery', 'jquery-ui', 'select2', 'bootstrap-table']
var ignoreCss = ['select2', 'css\/bootstrap', 'bootstrap-tab']

var readStream = fs.createReadStream(sourceFile)
var writeStream = fs.createWriteStream(destPath)
readStream.setEncoding('UTF8')
readStream.on('data', function (chunk) {
  data += chunk
  var reg = /\.\/static\//g
  data = data.replace(/\r\n/g, '')
  data = data.replace(reg, replaceText)
  data = data.replace(/<title>.+?<\/title>/, '')
  data = data.replace(/<\!DOCTYPE html>/, '')
  data = data.replace(/<html>/, '')
  data = data.replace(/<\/html>/, '')
  data = data.replace(/<body>/, '')
  data = data.replace(/<\/body>/, '')

  ignoreJs.forEach(function (item) {
    var jsReg = new RegExp('(<script){1} (src=https){1}(.+?)' + item + '(.+?)(script>){1}', 'm')
    data = data.replace(jsReg, '')
  })
  ignoreJs1.forEach(function (item) {
    var jsReg = new RegExp('(<script.+?' + item + '.+?<\/script>)', 'm')
    data = data.replace(jsReg, '')
  })
  ignoreCss.forEach(function (item) {
    // var cssReg = new RegExp('<link.+?' + item + '.+?.css>', 'im')
    var cssReg = new RegExp('<link .+?' + item + '.+?(.css){1}>', 'im')
    data = data.replace(cssReg, '')
  })
  writeStream.write(data, 'UTF8')
  writeStream.end()
})
// readStream.pipe(writeStream); 仅流式复制，不修改
writeStream.on('finish', function () {
  console.log('\x1B[32m leaderview：main.html生成成功 \x1B[0m')
})
writeStream.on('error', function (err) {
  console.log(err.stack)
})