'use strict'
require('./check-versions')()
process.env.NODE_ENV = 'production'

const fs = require('fs')
const ora = require('ora')
const rm = require('rimraf')
const path = require('path')
const chalk = require('chalk')
const webpack = require('webpack')
const config = require('../config')
const webpackConfig = require('./webpack.prod.conf')

const spinner = ora('building for production...')
spinner.start()

rm(path.join(config.build.assetsRoot, config.build.assetsSubDirectory), err => {
  if (err) throw err
  webpack(webpackConfig, (err, stats) => {
    spinner.stop()
    if (err) throw err
    process.stdout.write(stats.toString({
      colors: true,
      modules: false,
      children: false, // If you are using ts-loader, setting this to true will make TypeScript errors show up during build.
      chunks: false,
      chunkModules: false
    }) + '\n\n')

    if (stats.hasErrors()) {
      console.log(chalk.red('  Build failed with errors.\n'))
      process.exit(1)
    }

    // 打包之后自动生成simo项目中的大屏入口main.html文件
    var fileName = 'index.html'
    var targetName = 'main.html'
    // path.resolve(__dirname, '..')
    var sourceFile = path.resolve(__dirname, '../dist', fileName)
    var destPath = path.resolve(__dirname, '../dist', targetName)
    var data = ''
    // var arguments = process.argv.splice(2)
    var replaceText = '/leaderview-static/' // 目标目录

    // var ignoreJs = ['jquery', 'jquery-ui', 'bootstrap', 'topo', 'echarts']
    // var ignoreCss = ['bootstrap', 'mainStyle', 'layout', 'spectrum', 'flexStyle', 'echarts']
    var ignoreJs = ['bootstrap', 'jquery']
    var ignoreCss = ['bootstrap']

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
        // var jsReg = new RegExp('(<script){1} (src=https){1}(.+?)' + item + '(.+?)(script>){1}', 'm')
        var jsReg = new RegExp('<script[^>]*' + item + '[^>]*>(.*?)<\/script>', 'ig')
        data = data.replace(jsReg, '')
      })
      ignoreCss.forEach(function (item) {
        // var cssReg = new RegExp('<link[^>]*' + item + '[^>]*>(.*?)>', 'ig')
        var cssReg = new RegExp('<link [^>]*' + item + '[^>]*>', 'igm')
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

    console.log(chalk.cyan('  Build complete.\n'))
    console.log(chalk.yellow(
      '  Tip: built files are meant to be served over an HTTP server.\n' +
      '  Opening index.html over file:// won\'t work.\n'
    ))
  })
})
