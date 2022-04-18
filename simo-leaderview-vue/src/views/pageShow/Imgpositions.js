let header = process.env.NODE_ENV === 'development' ? './static/img/' : './img/'
let Imgpositions =
{
  'polygon': [{
    name: '第一管控区',
    color: Cesium.Color.fromCssColorString('#ff7575'),
    positions: [30.64269364, 104.04353678, 30.64186288, 104.04166460, 30.64057056, 104.04232174, 30.63927823, 104.04364944, 30.63878898, 104.04416978, 30.63848897, 104.04448628, 30.63824435, 104.04489398, 30.63791203, 104.04556990, 30.63862167, 104.04601648, 30.64048748, 104.04719532, 30.64231518, 104.04836476, 30.64296133, 104.04691637, 30.64339286, 104.04608756, 30.64365363, 104.04568791]
  }, {
    name: '第二管控区',
    color: Cesium.Color.fromCssColorString('#ff7575'),
    positions: [30.64333517, 104.04909164, 30.64311133, 104.04986680, 30.64282749, 104.05066073, 30.64287249, 104.05065939, 30.64321287, 104.05087933, 30.64321864, 104.05093029, 30.64294633, 104.05152977, 30.64277210, 104.05194014, 30.64279287, 104.05200183, 30.64301902, 104.05213594, 30.64305710, 104.05213997, 30.64307325, 104.05230358, 30.64297172, 104.05259460, 30.64298556, 104.05274212, 30.64262211, 104.05255973, 30.64244672, 104.05293927, 30.64232903, 104.05287623, 30.64229672, 104.05294329, 30.64219980, 104.05290037, 30.64188365, 104.05383915, 30.64191711, 104.05411676, 30.64198749, 104.05430853, 30.64204518, 104.05462235, 30.64195980, 104.05486107, 30.64065364, 104.05405104, 30.64072287, 104.05375868, 30.64064672, 104.05360579, 30.64023940, 104.05330002, 30.64017132, 104.05330271, 30.64048287, 104.05269116, 30.64069056, 104.05223384, 30.64075979, 104.05206889, 30.64154672, 104.05031741, 30.64212134, 104.04905140, 30.64235672, 104.04852837]

  }, {
    name: '第三管控区',
    color: Cesium.Color.fromCssColorString('#ff7575'),
    positions: [30.64033979, 104.03817773, 30.63737662, 104.03878927, 30.63550267, 104.03929353, 30.63361022, 104.03929353, 30.63292708, 104.04074192, 30.63816588, 104.04414833, 30.63887091, 104.04331952, 30.63934746, 104.04283941, 30.64051979, 104.04173970, 30.64101826, 104.04141515, 30.64152711, 104.04121533, 30.64171749, 104.04118180, 30.64106211, 104.03951347, 30.64090172, 104.03917819, 30.64077479, 104.03890997, 30.64059479, 104.03858274, 30.64036864, 104.03815627, 30.64034440, 104.03817371]
  }, {
    name: '第四管控区',
    color: Cesium.Color.fromCssColorString('#ff7575'),
    positions: [30.64423285, 104.04952347, 30.64408516, 104.04940814, 30.64303518, 104.04878318, 30.64240288, 104.04841840, 30.64283672, 104.04742330, 30.64307210, 104.04694319, 30.64344479, 104.04621363, 30.64354748, 104.04603124, 30.64368363, 104.04581130, 30.64373901, 104.04600441, 30.64381747, 104.04633701, 30.64384516, 104.04647648, 30.64388670, 104.04670984, 30.64396285, 104.04710144, 30.64400901, 104.04757887, 30.64402516, 104.04782563, 30.64413593, 104.04875368]
  }, {
    name: '第四管控区',
    color: Cesium.Color.fromCssColorString('#ff7575'),
    positions: [30.64432746, 104.05376673, 30.64514436, 104.05409932, 30.64519282, 104.05411810, 30.64600048, 104.05424684, 30.64609509, 104.05365676, 30.64423977, 104.04966831, 30.64404131, 104.04951006, 30.64334210, 104.04909432, 30.64311133, 104.04986680, 30.64283210, 104.05066341, 30.64287249, 104.05065939, 30.64321287, 104.05087933, 30.64321864, 104.05093029, 30.64294633, 104.05152977, 30.64277210, 104.05194014, 30.64279287, 104.05199647, 30.64301902, 104.05213594, 30.64305710, 104.05213997, 30.64307325, 104.05230358, 30.64297518, 104.05259728, 30.64298556, 104.05274212]
  }, {
    name: '第五管控区',
    color: Cesium.Color.fromCssColorString('#ff7575'),
    positions: [30.63855821, 104.04610097, 30.63828474, 104.04667899, 30.63810704, 104.04718995, 30.63791203, 104.04715776, 30.63747932, 104.04706925, 30.63723354, 104.04695928, 30.63759355, 104.04622972, 30.63787857, 104.04567048, 30.63855821, 104.04610097, 30.63861936, 104.04600978]
  }, {
    name: '第五管控区',
    color: Cesium.Color.fromCssColorString('#ff7575'),
    positions: [30.63723354, 104.04695928, 30.63747816, 104.04707193, 30.63791203, 104.04715776, 30.63810588, 104.04719532, 30.63828589, 104.04668033, 30.63855821, 104.04610097, 30.64027056, 104.04719532, 30.64169442, 104.04810324, 30.64227134, 104.04848009, 30.64094210, 104.05141443, 30.64041364, 104.05262947, 30.64002363, 104.05335367, 30.63968901, 104.05394912, 30.63918592, 104.05508369, 30.63968901, 104.05394912, 30.64002363, 104.05335367, 30.63983670, 104.05367553, 30.63782433, 104.05264556, 30.63802280, 104.05205548, 30.63799049, 104.05184627, 30.63549344, 104.05037105]
  }, {
    name: '第六管控区',
    color: Cesium.Color.fromCssColorString('#d0d000'),
    positions: [30.64672275, 104.05993581, 30.64638585, 104.05973196, 30.64525974, 104.05904531, 30.64527820, 104.05889511, 30.64532897, 104.05858934, 30.64542589, 104.05799925, 30.64552742, 104.05737162, 30.64558742, 104.05691564, 30.64564742, 104.05648649, 30.64571665, 104.05613244, 30.64590125, 104.05490398, 30.64600740, 104.05424416, 30.64609048, 104.05364871, 30.64641123, 104.05428439, 30.64665353, 104.05481279, 30.64675737, 104.05502737, 30.64684736, 104.05519098, 30.64696505, 104.05542433, 30.64718427, 104.05575693, 30.64746810, 104.05607611, 30.64769193, 104.05631483, 30.64819498, 104.05675471, 30.64818113, 104.05681908, 30.64721196, 104.05805022, 30.64703428, 104.05843109, 30.64692582, 104.05901045, 30.64682660, 104.05992776]
  }, {
    name: '第七管控区',
    color: Cesium.Color.fromCssColorString('#d0d000'),
    positions: [30.64600740, 104.05424953, 30.64514436, 104.05409932, 30.64297979, 104.05274749, 30.64262211, 104.05255973, 30.64243518, 104.05294061, 30.64232903, 104.05287623, 30.64229672, 104.05294329, 30.64219980, 104.05290037, 30.64188365, 104.05383915, 30.64191711, 104.05411676, 30.64198749, 104.05430853, 30.64204518, 104.05462235, 30.64195980, 104.05486107, 30.64065364, 104.05405104, 30.64072287, 104.05375868, 30.64064672, 104.05360579, 30.64023940, 104.05330002, 30.64017132, 104.05330271, 30.63928977, 104.05515879, 30.64525974, 104.05904531]
  }, {
    name: '第八管控区',
    color: Cesium.Color.fromCssColorString('#d0d000'),
    positions: [30.63983670, 104.05367017, 30.63782433, 104.05264556, 30.63802280, 104.05205548, 30.63798588, 104.05184627, 30.63549344, 104.05037105, 30.63457953, 104.05220032, 30.63658273, 104.05349851, 30.63918592, 104.05508369]
  }, {
    name: '第九管控区',
    color: Cesium.Color.fromCssColorString('#d0d000'),
    positions: [30.64033979, 104.03817773, 30.63737662, 104.03878927, 30.63550267, 104.03929353, 30.63361945, 104.03929353, 30.63292708, 104.04074192, 30.63141308, 104.03986216, 30.63337943, 104.03838158, 30.63521650, 104.03691173, 30.63578423, 104.03649330, 30.63597347, 104.03637528, 30.63610271, 104.03633237, 30.63697968, 104.03616071, 30.63812896, 104.03501809, 30.63814742, 104.03500199]
  }],
  'police':[ {
    name: '公安第一网格',
    color: Cesium.Color.LAVENDAR_BLUSH,
    positions: [30.64231518, 104.04836476, 30.64227134, 104.04848009, 30.64235672, 104.04852837, 30.64404131, 104.04951006, 30.64423977, 104.04966831, 30.64423054, 104.04952079, 30.64408516, 104.04940814, 30.64303518, 104.04878318, 30.64240288, 104.04841840]
  }, {
    name: '公安第二网格',
    color: Cesium.Color.LEMONCHIFFON,
    positions: [30.64235672, 104.04852837, 30.64212134, 104.04905140, 30.64190442, 104.04952615, 30.64188365, 104.04956907, 30.64154672, 104.05031741, 30.64075979, 104.05206889, 30.64069056, 104.05222982, 30.64060979, 104.05216277, 30.64094210, 104.05141443, 30.64149595, 104.05019134, 30.64195288, 104.04916137, 30.64227134, 104.04848009]
  }, {
    name: '公安第三网格',
    color: Cesium.Color.LIMEGREEN,
    positions: [30.64069056, 104.05222982, 30.64048287, 104.05269116, 30.63994747, 104.05370235, 30.63993594, 104.05380160, 30.63986440, 104.05401886, 30.63965208, 104.05448288, 30.63934746, 104.05518562, 30.63918592, 104.05508369, 30.63968901, 104.05394912, 30.64002363, 104.05335367, 30.64039517, 104.05263215, 30.64060979, 104.05216277]
  }, {
    name: '公安第四网格',
    color: Cesium.Color.MAGENTA,
    positions: [30.64231518, 104.04836476, 30.64227134, 104.04848009, 30.64169442, 104.04810324, 30.64032594, 104.04724360, 30.64047825, 104.04719397, 30.64056941, 104.04725567, 30.64143595, 104.04780954]
  }, {
    name: '公安第五网格',
    color: Cesium.Color.LIME,
    positions: [30.64032594, 104.04724360, 30.64025902, 104.04718593, 30.63936131, 104.04661193, 30.63828820, 104.04593199, 30.63806319, 104.04579654, 30.63811281, 104.04569194, 30.63862167, 104.04601648, 30.63866206, 104.04604733, 30.63886514, 104.04617608, 30.63948823, 104.04656366, 30.64016902, 104.04699951, 30.64047825, 104.04719397]
  }, {
    name: '公安第六网格',
    color: Cesium.Color.MEDIUMBLUE,
    positions: [30.64367671, 104.04580593, 30.64354748, 104.04603124, 30.64355209, 104.04602855, 30.64344479, 104.04621363, 30.64327979, 104.04653415, 30.64306287, 104.04694855, 30.64283672, 104.04742330, 30.64240288, 104.04841840, 30.64231518, 104.04836476, 30.64276980, 104.04732943, 30.64296133, 104.04691637, 30.64339286, 104.04608756, 30.64363517, 104.04568791]
  }],
  'keyPlaces':[{
    name: '三六三医院重点区域',
    color: Cesium.Color.GREEN,
    positions: [30.65040096, 104.05480742, 30.64988177, 104.05425355, 30.64947219, 104.05383915, 30.64845342, 104.05534387, 30.64888262, 104.05582666, 30.64953334, 104.05527413]
  }, {
    name: '天朗锦邸重点区域',
    color: Cesium.Color.GREEN,
    positions: [30.64927490, 104.04145539, 30.64896108, 104.04146612, 30.64853649, 104.04226005, 30.64936720, 104.04280186, 30.64976871, 104.04193819]
  }, {
    name: '金科双楠重点区域',
    color: Cesium.Color.GREEN,
    positions: [30.64118903, 104.02803898, 30.64093518, 104.02674079, 30.63998901, 104.02772248, 30.64052902, 104.02844131]
  }, {
    name: '美领馆区域',
    color: Cesium.Color.GREEN,
    positions: [30.62499680, 104.06697392, 30.62378735, 104.06703830, 30.62373657, 104.06900167, 30.62419358, 104.06913579, 30.62503372, 104.06913042]
  }, {
    name: '数码广场守护区',
    color: Cesium.Color.GREEN,
    positions: [30.63421950, 104.06675398, 30.63423796, 104.06787515, 30.63404872, 104.06798244, 30.63403487, 104.06870127, 30.63254858, 104.06873345, 30.63254858, 104.06678081]
  },{
    name: '罗马假日广场区域',
    color: Cesium.Color.GREEN,
    positions: [30.63816588, 104.04414833, 30.63758432, 104.04375672, 30.63709507, 104.04342413, 30.63707199, 104.04340267, 30.63633349, 104.04293060, 30.63678582, 104.04196501, 30.63695198, 104.04161096, 30.63846590, 104.04267848, 30.63853051, 104.04271066, 30.63854898, 104.04273748, 30.63852705, 104.04277906, 30.63841051, 104.04307947, 30.63887091, 104.04331952]
  }],
  'markers': {
    // 常规地点
    'didian': [
      {
        'name': '武侯名苑',
        'img': header + 'hongqi.png',
        'label': '武侯名苑',
        'Lat': 30.639311,
        'Lng': 104.045897
      },
      {
        'name': '锅庄舞场',
        'img': header + 'hongqi.png',
        'label': '锅庄舞场',
        'Lat': 30.639608,
        'Lng': 104.043361
      },
      {
        'name': '锦江苑',
        'img': header + 'hongqi.png',
        'label': '锦江苑',
        'Lat': 30.638002,
        'Lng': 104.046503
      },
      {
        'name': '锦宏阁',
        'img': header + 'hongqi.png',
        'label': '锦宏阁',
        'Lat': 30.637744,
        'Lng': 104.046206
      },
      {
        'name': '洗面桥横街22号院',
        'img': header + 'hongqi.png',
        'label': '洗面桥横街22号院',
        'Lat': 30.640732,
        'Lng': 104.052570
      },
      {
        'name': '成都A区',
        'img': header + 'hongqi.png',
        'label': '成都A区',
        'Lat': 30.638411,
        'Lng': 104.040366
      },
      {
        'name': '风尚国际',
        'img': header + 'hongqi.png',
        'label': '风尚国际',
        'Lat': 30.634201,
        'Lng': 104.040077
      },
      {
        'name': '罗孚世家',
        'img': header + 'hongqi.png',
        'label': '罗孚世家',
        'Lat': 30.636943,
        'Lng': 104.039240
      },
      {
        'name': '锦和名邸',
        'img': header + 'hongqi.png',
        'label': '锦和名邸',
        'Lat': 30.645541,
        'Lng': 104.055328
      },
      {
        'name': '天亿大厦',
        'img': header + 'hongqi.png',
        'label': '天亿大厦',
        'Lat': 30.645068,
        'Lng': 104.058391
      },
      {
        'name': '罗马假日广场',
        'img': header + 'hongqi.png',
        'label': '罗马假日广场',
        'Lat': 30.637990,
        'Lng': 104.043853
      },
      {
        'name': '武侯祠横街',
        'img': header + 'hongqi.png',
        'label': '武侯祠横街',
        'Lat': 30.640603,
        'Lng': 104.047319
      },
      {
        'name': '耍都',
        'img': header + 'hongqi.png',
        'label': '耍都',
        'Lat': 30.646229,
        'Lng': 104.057581
      },
      {
        'name': '洗面桥横街',
        'img': header + 'hongqi.png',
        'label': '洗面桥横街',
        'Lat': 30.641687,
        'Lng': 104.049797
      },
      {
        'name': '西南民族大学',
        'img': header + 'hongqi.png',
        'label': '西南民族大学',
        'Lat': 30.638263,
        'Lng': 104.049647
      },
      {
        'name': '东华电脑城',
        'img': header + 'hongqi.png',
        'label': '东华电脑城',
        'Lat': 30.633763,
        'Lng': 104.068015
      },
      {
        'name': '来福士广场',
        'img': header + 'hongqi.png',
        'label': '来福士广场',
        'Lat': 30.632946,
        'Lng': 104.067242
      },
      {
        'name': '德国驻成都领事馆',
        'img': header + 'hongqi.png',
        'label': '德国驻成都领事馆',
        'Lat': 30.624623,
        'Lng': 104.067559
      },
      {
        'name': '泰国驻成都领事馆',
        'img': header + 'hongqi.png',
        'label': '泰国驻成都领事馆',
        'Lat': 30.618719,
        'Lng': 104.068514
      }],
    // 封控
    'fengkong': [
      {
        'name': '封控点',
        'img': header + '封控点.png',
        'label': '封控点',
        'Lat': 30.643655,
        'Lng': 104.045767
      },
      {
        'name': '封控点',
        'img': header + '封控点.png',
        'label': '封控点',
        'Lat': 30.639253,
        'Lng': 104.055108
      },
      {
        'name': '封控点',
        'img': header + '封控点.png',
        'label': '封控点',
        'Lat': 30.638079,
        'Lng': 104.045746
      },
      {
        'name': '巡逻组',
        'img': header + '封控点.png',
        'label': '巡逻组',
        'Lat': 30.637912,
        'Lng': 104.042963
      },
      {
        'name': '第三备勤点',
        'img': header + '封控点.png',
        'label': '第三备勤点',
        'Lat': 30.637134,
        'Lng': 104.042979
      },
      {
        'name': '外围巡逻组',
        'img': header + '封控点.png',
        'label': '外围巡逻组',
        'Lat': 30.636710,
        'Lng': 104.042072
      },
      {
        'name': '封控组',
        'img': header + '封控点.png',
        'label': '封控组',
        'Lat': 30.643986,
        'Lng': 104.049443
      }],
    // 备勤
    'beiqing': [
      {
        'name': '第一备勤点',
        'img': header + 'beiqing.png',
        'label': '第一备勤点',
        'Lat': 30.645401,
        'Lng': 104.059737
      },
      {
        'name': '第二备勤点',
        'img': header + 'beiqing.png',
        'label': '第二备勤点',
        'Lat': 30.642710,
        'Lng': 104.047206
      }],
    // 现场指挥车
    'xianchangzhihui': [
      {
        'name': '现场指挥部',
        'img': header + '现场指挥车.png',
        'label': '现场指挥部',
        'Lat': 30.635833,
        'Lng': 104.050755
      },
      {
        'name': '现场指挥车',
        'img': header + '现场指挥车.png',
        'label': '现场指挥车',
        'Lat': 30.642320,
        'Lng': 104.048397
      },
      {
        'name': '华西坝派出所巡控组',
        'img': header + '现场指挥车.png',
        'label': '华西坝派出所巡控组',
        'Lat': 30.639474,
        'Lng': 104.055288
      },
      {
        'name': '浆洗所巡控组',
        'img': header + '现场指挥车.png',
        'label': '浆洗所巡控组',
        'Lat': 30.643169,
        'Lng': 104.044304
      }],
    // 巡大快反
    'xundakuaifan': [
      {
        'name': '巡大快反1组',
        'img': header + '巡大快反1组.png',
        'label': '巡大快反1组',
        'Lat': 30.639509,
        'Lng': 104.054789
      },
      {
        'name': '摩巡组',
        'img': header + '巡大快反1组.png',
        'label': '摩巡组',
        'Lat': 30.641241,
        'Lng': 104.050968
      },
      {
        'name': '摩巡组',
        'img': header + '巡大快反1组.png',
        'label': '摩巡组',
        'Lat': 30.640335,
        'Lng': 104.047265
      }, {
        'name': '巡大快反1组',
        'img': header + '巡大快反1组.png',
        'label': '巡大快反1组',
        'Lat': 30.644459,
        'Lng': 104.049473
      } ],
    // 华西快反
    'huaxikuaifan': [
      {
        'name': '华西快反1组',
        'img': header + '华西快反1组.png',
        'label': '华西快反1组',
        'Lat': 30.639214,
        'Lng': 104.055400
      },
      {
        'name': '固定卡点',
        'img': header + '华西快反1组.png',
        'label': '固定卡点',
        'Lat': 30.636262,
        'Lng': 104.042906
      },
      {
        'name': '固定卡点',
        'img': header + '华西快反1组.png',
        'label': '固定卡点',
        'Lat': 30.637467,
        'Lng': 104.040149
      }],
    // 消防战车
    'xiaofangzhanche': [{
      'name': '消防战车',
      'img': header + '消防战车.png',
      'label': '消防战车',
      'Lat': 30.644281,
      'Lng': 104.049749
    }]
  },
  'pointBase': [
    {
      'name': '第六管控区',
      'id': 6,
      'img': [header + 'location.png', header + '第六管控区.png'],
      'Lat': 30.646654,
      'Lng': 104.057157
    }, {
      'name': '第七管控区',
      'id': 7,
      'img': [header + 'location.png', header + '第七管控区.png'],
      'Lat': 30.644060,
      'Lng': 104.055762
    }, {
      'name': '第二管控区',
      'id': 2,
      'img': [header + 'location.png', header + '第二管控区.png'],
      'Lat': 30.642107,
      'Lng': 104.051718
    }, {
      'name': '第四管控区',
      'id': 4,
      'img': [header + 'location.png', header + '第四管控区.png'],
      'Lat': 30.643547,
      'Lng': 104.047898
    }, {
      //   'name': '第四管控区',
      //   'id':4,
      //   'img': [header + 'location.png', header + '第四管控区.png'],
      //   'Lat': 30.644415,
      //   'Lng': 104.051787
      // }, {
      'name': '第一管控区',
      'id': 1,
      'img': [header + 'location.png', header + '第一管控区.png'],
      'Lat': 30.641226,
      'Lng': 104.045849
    }, {
      'name': '第八管控区',
      'id': 8,
      'img': [header + 'location.png', header + '第八管控区.png'],
      'Lat': 30.637247,
      'Lng': 104.052908
    }, {
      'name': '第五管控区',
      'id': 5,
      'img': [header + 'location.png', header + '第五管控区.png'],
      'Lat': 30.639204,
      'Lng': 104.049840
    }, {
      'name': '第三管控区',
      'id': 3,
      'img': [header + 'location.png', header + '第三管控区.png'],
      'Lat': 30.638374,
      'Lng': 104.040849
    }, {
      'name': '第九管控区',
      'id': 9,
      'img': [header + 'location.png', header + '第九管控区.png'],
      'Lat': 30.637234,
      'Lng': 104.037480
    }],
  'policePoint':[
    {
      'name': '公安第一网格',
      'img': header + 'hongqi.png',
      'id': '公安第一网格',
      'Lat': 30.643619,
      'Lng': 104.049218
    }, {
      'name': '公安第二网格',
      'img': header + 'hongqi.png',
      'id': '公安第二网格',
      'Lat': 30.641685,
      'Lng': 104.049923
    }, {
      'name': '公安第三网格',
      'img': header + 'hongqi.png',
      'id': '公安第三网格',
      'Lat': 30.639754,
      'Lng': 104.054038
    }, {
      'name': '公安第四网格',
      'img': header + 'hongqi.png',
      'id': '公安第四网格',
      'Lat': 30.641535,
      'Lng': 104.047944
    }, {
      'name': '公安第五网格',
      'img': header + 'hongqi.png',
      'id': '公安第五网格',
      'Lat': 30.639518,
      'Lng': 104.046654
    }, {
      'name': '公安第六网格',
      'img': header + 'hongqi.png',
      'id': '公安第六网格',
      'Lat': 30.643061,
      'Lng': 104.046890
    }],
  'keyPlacesPoint':[ {
    'name': '美领馆守护区',
    'img': header + 'location.png',
    'Lat': 30.624498,
    'Lng': 104.068583
  }, {
    'name': '数码广场守护区',
    'img': header + 'location.png',
    'Lat': 30.633947,
    'Lng': 104.067569
  }, {
    'name': '三六三医院重点区域',
    'img': header + 'location.png',
    'Lat': 30.649450,
    'Lng': 104.054834
  }, {
    'name': '天朗锦邸重点区域',
    'img': header + 'location.png',
    'Lat': 30.649196,
    'Lng': 104.042126
  }, {
    'name': '金科双楠重点区域',
    'img': header + 'location.png',
    'Lat': 30.640718,
    'Lng': 104.027722
  }]
}

export default Imgpositions
