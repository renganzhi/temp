export default {
  'item': {
    'text': '3D模型',
    'imgClass': 'icon-3dhistogram',
    'chartType': 'TDModel',
    'width': 600,
    'height': 400,
    lightColor: '#fff',
    ShowHelpLine: true,
    lightStrength: 1,
    cameraX: 20,
    cameraY: 20,
    cameraZ: 10,
    ModelScal: 1,
    gltfName: '',
    ModelRotation: 0,
    ZoomLimitMax: 50,
    ZoomLimitMin: 0
  },
  'styles': {
    'base': [{
      'name': '灯光配置',
      'tag': 'Hint',
      'childoption': [
        {
          'name': '灯光颜色',
          'key': 'lightColor',
          'tag': 'Color'
        },
        {
          'name': '灯光强度',
          'key': 'lightStrength',
          'tag': 'input'
        }]
    }, {
      'name': '摄像机配置',
      'tag': 'Hint',
      'childoption': [
        {
          'name': '摄像机X位置',
          'key': 'cameraX',
          'tag': 'input'
        },
        {
          'name': '摄像机Y位置',
          'key': 'cameraY',
          'tag': 'input'
        },
        {
          'name': '摄像机Z位置',
          'key': 'cameraZ',
          'tag': 'input'
        }]
    }, {
      'name': '模型配置',
      'tag': 'Hint',
      'childoption': [
        {
          'name': '辅助线显隐',
          'key': 'ShowHelpLine',
          'tag': 'select',
          'options': [
            { 'name': '显示', 'value': true },
            { 'name': '隐藏', 'value': false }
          ]
        },
        {
          'name': '模型放缩',
          'key': 'ModelScal',
          'tag': 'input'
        },
        {
          'name': '模型旋转',
          'key': 'ModelRotation',
          'tag': 'input'
        }]
    }, {
      'name': '放缩设置',
      'tag': 'Hint',
      'childoption': [
        {
          'name': '放缩上限',
          'key': 'ZoomLimitMax',
          'tag': 'input'
        },
        {
          'name': '放缩下限',
          'key': 'ZoomLimitMin',
          'tag': 'input'
        }]
    }
    ]
  }
}
