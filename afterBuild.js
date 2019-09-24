var fs = require('fs')
var path = require('path')

var fileName = 'index.html'
var targetName = 'main.html'
var sourceFile = path.join(__dirname, 'dist', fileName)
var destPath = path.join(__dirname, 'dist', targetName)
var data = ''
var arguments = process.argv.splice(2)
var replaceText = arguments[0] || '/leaderview-static/' // 目标目录

var readStream = fs.createReadStream(sourceFile)
var writeStream = fs.createWriteStream(destPath)
readStream.setEncoding('UTF8')
readStream.on('data', function (chunk) {
  data += chunk
  var reg = /\.\/static\//g
  data = data.replace(/\r\n/g, '')
  data = data.replace(reg, replaceText)
  writeStream.write(data, 'UTF8')
  writeStream.end()
})
// readStream.pipe(writeStream); 仅流式复制，不修改
writeStream.on('finish', function () {
  console.log('\x1B[32m main.html生成成功 \x1B[0m')
})
writeStream.on('error', function (err) {
  console.log(err.stack)
})