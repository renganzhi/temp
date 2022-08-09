export default {
  item: {
    text: 'iframe弹窗',
    chartType: 'IframePop',
    imgClass: 'icon-hotspot',
    width: 200,
    height: 50,
    popHeight: 200,
    popWidth: 200,
    popUrl: ''
  },
  styles: {
    base: [
      {
        name: '图元配置',
        tag: 'Hint',
        childoption: [{
          name: '弹窗宽度',
          key: 'popWidth',
          tag: 'input'
        }, {
          name: '弹窗宽度',
          key: 'popHeight',
          tag: 'input'
        }, {
          name: '弹窗地址',
          key: 'popUrl',
          tag: 'input',
          type: 'string'
        }]
      }
    ]
  }
}
