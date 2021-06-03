/*
 * opt={
 *  selector:'#tp',
 *  config:{}   //全局设置
 * }
 * */
// import levelMapName from './../topo/enum'
import { gbs } from '@/config/settings'
import { newAjax, getTopoIcon } from '@/config/thirdLoginMix'
function mapTopology (opt) {
  this.defaultConfig = {
    width: 24,
    height: 24,
    fontSize: 12,
    textColor: '#000',
    lineWidth: 1
  }
  this.tpId = opt.tpId || '01677d30-9c0b-42ae-a934-11fbae6dc908' // add
  this.isMonit = opt.isMonit
  this.config = $.extend({}, this.defaultConfig, opt.config)
  this.ele = d3.select(opt.selector)[0][0]
  this.canvasWidth = this.ele.clientWidth
  this.canvasHeight = this.ele.clientHeight
  this.force = d3.layout.force().distance(160).charge(-3000).size([this.canvasWidth, this.canvasHeight / 2])
  this.force.start()
  this.nodes = []
  this.links = []
  this.nodes = this.force.nodes()
  this.links = this.force.links()

  if (opt.isShow) {
    this.createMainSvg(opt.mpCode)
  }
  this.lineC = d3.svg.line().interpolate('linear')
  this.lineradial = d3.svg.line.radial()
    .radius(function (d) { return d.r })
    .angle(function (d) { return d.angle })
    .tension(0.1)
  this.showTip = true // 控制悬浮提示是否显示,默认显示
  this.isMarquee = false // 是否在框选
  this.iconChange = true // baseData.mapTopo.iconChange
}

mapTopology.prototype = {
  constructor: mapTopology,
  createMainSvg: function (mpCode) {
    var _this = this
    var toporoot, selectMapNum
    if ($('#home-html').length > 0) {
      this.tip = d3.select('#home-html').append('div').classed('tp-tip', true).classed('hide', true) // 提示信息
    } else {
      this.tip = d3.select('body').append('div').classed('tp-tip', true).classed('hide', true) // 提示信息
    }
    this.svgContainer = d3.select(this.ele).append('svg')
      .attr('width', '100%')
      .attr('height', '100%')
      .attr('viewBox', '0 0 ' + this.canvasWidth + ' ' + this.canvasHeight)
    // var mpCode = $('#map_topo_wrap').attr('mapId').substring($('#map_topo_wrap').attr('mapId').length - 6)
    mpCode = mpCode.substring(mpCode.length - 6)
    if (mpCode.substring(2, 6) === '0000') {
      selectMapNum = mpCode
    } else {
      var num = mpCode.substring(0, 2)
      selectMapNum = num + '0000/' + mpCode
    }
    newAjax({
      url: gbs.host + '/resources/data/mapJson/' + selectMapNum + '.geoJson',
      async: false,
      success: function (data) {
        toporoot = JSON.parse(data)
        toporoot.type = 'FeatureCollection'
      }
    })
    this.center = d3.geo.centroid(toporoot)
    this.bounds = d3.geo.bounds(toporoot)
    var extra = mpCode == '100000' ? _this.canvasHeight * 0.4 : this.flagGoe(mpCode) ? 0 : 110 // 这里又是啥？
    var geoDis = d3.geo.distance(this.bounds[0], this.bounds[1])
    this.scale = Math.floor((_this.canvasHeight + extra) / (geoDis > 3 ? (geoDis / 15) : geoDis) / Math.sqrt(2))
    this.projection = d3.geo.mercator()
      .center(this.center)
      .scale(this.scale)
      .translate([_this.canvasWidth / 2, _this.canvasHeight / 2])

    this.path = d3.geo.path()
      .projection(this.projection)
    var path = this.path

    this.defs = this.svgContainer.append('defs')

    this.arrowMarker = this.defs.append('marker')
      .attr('id', 'arrow')
      .attr('markerUnits', 'strokeWidth')
      .attr('markerWidth', '12')
      .attr('markerHeight', '12')
      .attr('viewBox', '0 0 12 12')
      .attr('refX', '6')
      .attr('refY', '6')
      .attr('orient', 'auto')

    this.arrow_path = 'M2,2 L10,6 L2,10 L6,6 L2,2'

    this.arrowMarker.append('path')
      .attr('d', this.arrow_path)
      .attr('fill', '#000')

    this.startMarker = this.defs.append('marker')
      .attr('id', 'startPoint')
      .attr('markerUnits', 'strokeWidth')
      .attr('markerWidth', '12')
      .attr('markerHeight', '12')
      .attr('viewBox', '0 0 12 12')
      .attr('refX', '6')
      .attr('refY', '6')
      .attr('orient', 'auto')

    this.startMarker.append('circle')
      .attr('cx', 6)
      .attr('cy', 6)
      .attr('r', 2)
      .attr('fill', '#000')

    this.copy = this.svgContainer.append('g').attr('class', 'containerDataCopy')
    this.china = this.svgContainer.append('g').attr('class', 'containerData')

    this.provinces = this.china.selectAll('path')
      .data(toporoot.features)
      .enter()
    this.provincesCopy = this.copy.selectAll('path')
      .data(toporoot.features)
      .enter()

    this.provinces.append('path')
      .attr('class', 'province')
      .style('fill', function (d) {
        return d.backColor
      })
      .attr('d', this.path)
    this.provincesCopy.append('path')
      .attr('class', 'provincesCopy')
      .style('fill', function (d) {
        return d.backColor
      })
      .attr('d', this.path)

    this.provinces.append('text').attr('class', 'mptText').text(function (d) {
      return d.properties.name
    })
      .each(function (d) {
        // var p = projection(d.properties.cp);
        var p = path.centroid(d.geometry)
        d.isBind = false
        d3.select(this).attr({
          x: p[0],
          y: p[1],
          idName: d.id
        })
      })
    // });
    this.nodeLine = this.china.append('g').attr('id', 'nodeLine')
    this.marqueeRect = this.svgContainer.append('rect').classed('marqueeRect', true)
    this.dragline = this.svgContainer.append('svg:path').attr({
      'class': 'link dragline hidden'
    })
    this.createLiquid()
    // _this = null
    if (selectMapNum === '100000') { // 全国地图加九段线
      var taiwan = _this.svgContainer.select('[idName="tai_wan"]')
      _this.svgContainer.append('g').attr('transform', 'translate(' + (Number(taiwan.attr('x')) + 30) + ',' + (taiwan.attr('y')) + ')scale(1)').attr('class', 'southsea').html(function (d) {
        return `<g xmlns="http://www.w3.org/2000/svg" id="页面-1" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
            <g id="Apple-TV" transform="translate(-372.000000, -445.000000)">
                <g id="九段线" transform="translate(373.000000, 446.000000)">
                    <path d="M11.2227544,16.5404157 C11.2227544,16.5404157 10.8424626,16.9442587 10.5889347,17.0826497 C10.3354068,17.2202315 10.1209445,17.3286783 10.1018103,17.4468367 C10.0810816,17.5649952 10.1105802,17.6345954 10.0810816,17.7325212 C10.0523804,17.8312564 10.091446,17.8612007 10.1209445,17.9494148 C10.1496458,18.0384383 10.1209445,18.1857318 10.0619474,18.2844669 C10.0037476,18.3823927 9.99418057,18.4511836 10.0332462,18.5709607 C10.0723118,18.6883098 10.0810816,18.9060127 10.0428133,19.1212877 C10.0037476,19.3389906 9.95511494,19.3786466 10.0810816,19.5558843 C10.2086428,19.7323126 10.3258397,19.7719685 10.3154753,20.1758115 C10.3059082,20.5796545 10.2469112,20.7771248 10.3936066,20.807069 C10.540302,20.8370133 11.0864234,20.9948276 11.2131873,21.1429304 C11.3399513,21.2910331 11.4077182,21.3298797 11.5647779,21.3193587 C11.7202431,21.3104564 11.8175085,21.3298797 11.914774,21.5071174 C12.0128366,21.6835458 12.0614693,21.7636669 12.1786662,21.7337227 C12.2958631,21.7045877 12.9001843,21.5856199 13.0365153,21.7830902 C13.1736436,21.9805605 13.1832107,22 13.3394732,21.9700395 C13.4957357,21.9409046 13.5539355,21.8721137 13.6017709,21.9506163 C13.6512009,22.0291188 13.7094007,22 13.8074634,21.9595186 C13.9047288,21.920672 13.9342273,21.902058 14.0514242,21.8721137 C14.1678238,21.8421695 14.1295554,21.7733786 14.1295554,21.7733786 C14.1295554,21.7733786 14.0705583,21.6754527 14.1486896,21.6058525 C14.2268208,21.5370616 14.2650892,21.4286149 14.2850206,21.3695356 C14.3041548,21.3104564 14.3145191,21.2797028 14.4612145,21.2708005 C14.6071127,21.2610888 14.694811,21.3193587 14.8606406,21.1518327 C15.0264701,20.9843067 15.0647385,20.9843067 15.0647385,21.1121768 C15.0647385,21.2416655 15.0751028,21.3395914 15.143667,21.2505678 C15.2114339,21.1623537 15.2512968,21.1226978 15.3676964,21.0935628 C15.4848932,21.0636185 15.4753261,20.9656927 15.4848932,20.8863808 C15.4944603,20.807069 15.6220215,20.4623053 15.7774867,20.4234587 C15.9337492,20.3838028 15.9536806,20.3732818 16.0509461,20.246221 C16.0963897,20.1855231 16.4431968,19.8747502 16.4695063,20.0884066 C16.4798706,20.1660999 16.5675689,20.1660999 16.6457002,20.0884066 C16.7230342,20.0082855 16.8593652,19.8618014 16.9964935,19.7622569 C17.1328245,19.6635217 17.1822545,19.6052518 17.2109558,19.4377258 C17.2404543,19.2701998 17.1623231,19.2596788 17.1232574,19.358414 C17.0849891,19.4571491 16.9964935,19.5170376 16.966995,19.4183025 C16.9382937,19.3195673 16.9765621,19.2297346 17.0451262,19.1423297 C17.1136904,19.0524969 17.1910243,18.8946825 17.1910243,18.7862357 C17.1910243,18.6785982 17.1910243,18.6397516 17.2508187,18.5410164 C17.3090185,18.4422813 17.3377197,18.4422813 17.3377197,18.2844669 C17.3377197,18.1266525 17.5139136,17.7122886 17.6111791,17.6240744 C17.7084445,17.5350509 17.7865757,17.4168925 17.7770086,17.3489109 C17.7666443,17.2793107 17.7084445,17.1303987 17.8352084,17.0713195 C17.9619724,17.0130495 18.2449988,16.7961559 18.2545659,16.6585742 C18.264133,16.5209924 18.2155003,16.4311596 18.1860017,16.3623687 C18.1573005,16.2935778 18.0887363,16.2539219 18.2059332,16.2053637 C18.32313,16.1551868 18.5479567,15.9787584 18.4985267,16.0863959 C18.4490967,16.1948427 18.3613984,16.3324245 18.4012613,16.4222573 C18.4403269,16.5104714 18.4403269,16.5800716 18.5080938,16.5800716 C18.5766579,16.5800716 18.5670908,16.5404157 18.5854278,16.4416806 C18.6061565,16.3429454 18.7321232,16.2150753 18.8198215,16.1260518 C18.9083171,16.0378376 19.0446481,16.0087027 18.9856511,15.8889256 C18.9274513,15.7715764 18.8397529,15.3175566 18.8110517,15.0812397 C18.7807559,14.8449228 18.6149263,14.5495266 18.4690282,14.5301033 C18.32313,14.5090614 17.8543426,14.5001591 17.8160742,14.2638422 C17.7770086,14.0275253 17.7770086,13.9279808 17.6207462,14.0566602 C17.4644837,14.1853396 17.3185855,14.3229214 17.3281526,14.5090614 C17.3377197,14.6976294 17.2508187,14.7753226 17.1822545,14.6773968 C17.1136904,14.5786616 16.9964935,14.4216565 16.8697296,14.3625773 C16.7429656,14.3026888 16.6648344,14.2241862 16.5380704,14.2937864 C16.4113065,14.3625773 16.2159784,14.3528657 16.0015161,14.3026888 C15.7878511,14.2541305 15.6993555,14.2832655 15.6307913,14.3625773 C15.5630244,14.4402706 15.5630244,14.4904474 15.5630244,14.5495266 C15.5630244,14.6077966 15.4553947,14.6879177 15.3676964,14.7461877 C15.279998,14.8052669 15.143667,14.8651554 15.0559687,14.7963645 C14.9674731,14.7275736 14.8510735,14.6579735 14.7825093,14.736476 C14.7147424,14.8149785 14.6852439,14.9428486 14.5967483,14.8449228 C14.50905,14.7461877 14.4508502,14.7065317 14.3248835,14.6879177 C14.1973223,14.6676851 14.1000569,14.6280292 14.0314927,14.5681406 C13.9637258,14.5090614 13.9246602,14.5001591 13.7588306,14.6077966 C13.5930011,14.7170527 13.3777415,14.9541789 13.4072401,15.0812397 C13.4367386,15.2091098 13.4758042,15.2779007 13.3681744,15.3086543 C13.2613419,15.3377892 13.0668111,15.3774452 13.0365153,15.2876123 C13.0070168,15.2002075 12.9591813,15.1103747 12.7638532,15.1014723 C12.5685251,15.0917607 12.2958631,15.1217049 12.236866,15.2398634 C12.1786662,15.3580218 12.1196691,15.4664686 12.0710364,15.5838178 C12.0224037,15.7027856 12.0423352,15.7715764 12.159532,15.7027856 C12.2759316,15.6339947 12.3540629,15.5943388 12.373197,15.6526087 C12.3931285,15.7124972 12.3835614,15.8015207 12.2759316,15.8492697 C12.1690991,15.8994465 12.1396006,15.8889256 12.0710364,15.9981817 C12.0032695,16.1066285 11.8278729,16.166517 11.6811775,16.1948427 C11.4013401,16.2523033 11.3933675,16.4206386 11.2227544,16.5404157 Z" id="Stroke-1" fill="#0F1A3F"/>
                    <path d="M43,0 L38.3912945,3.07522234 L35.9127446,3.4597906 L31.6600176,4.83301967 C31.3854256,4.96733376 31.0865107,4.88060541 30.7632732,4.57283455 C30.4400357,4.26506368 30.3803181,5.23334444 29.9017355,4.38189452 C29.9046939,4.66300997 29.5752342,4.62624136 29.5605429,4.83301967 C29.4755779,6.02889527 29.3626183,5.93663419 29.0623315,6.25567119 C27.8500823,7.54361464 25.1870447,7.31927484 24.5984708,7.63108589 C21.5266945,9.25843262 19.6567842,8.33310439 18.7966481,9.1193531 C18.3138252,9.56070057 17.2263717,9.99874214 17.3293304,10.5667452 C17.456465,11.2681214 18.7576872,12.2572823 18.7089433,12.6728416 C18.5760412,13.8058803 15.6883062,11.6793912 15.43469,11.0853233 L15.43469,11.0853233 L15.664,8.307 L16.231,7.564 L16.2067485,7.56765667 C16.1744189,7.57012006 16.1397048,7.57112846 16.1025111,7.57112846 C15.9165428,7.57112846 15.743312,7.88535415 15.643959,8.17436977 C15.6284568,8.21971249 15.6144689,8.25727262 15.6005521,8.28832058 L13.5822005,8.6116425 L12.027473,7.63108589 L10.9606508,6.92880585 L10.9606508,7.63108589 L8.3035233,8.38092835 L7.11961518,8.38092835 L5.48573409,7.16618039 L4.48390258,6.42395486 L4.09946025,4.57283455 L4.48390258,2.83400339 L2.58858813,2.74854276 L0,2.21686324 L0,0 L43,0 Z M22,0 L16.231,7.564 L16.296679,7.55545499 C16.3519906,7.54381514 16.3981447,7.52456904 16.4359022,7.49414407 L16.4863362,7.43967588 C16.5610631,7.33613447 16.6111642,7.32172872 17.0323528,7.33613447 C17.4535413,7.3487395 17.6140345,7.24429773 17.5520451,6.95528211 C17.4900557,6.66806724 17.3660767,6.26200482 17.4289154,6.16926773 C17.4900557,6.07833135 17.501944,6.05132054 17.8246288,6.15756305 C18.1464644,6.26200482 18.543027,6.22238897 18.7544705,6.0387155 C18.9642156,5.8559424 19.0890436,5.64525812 18.9769531,5.48769508 C18.8657118,5.33193278 18.6304916,5.00330131 18.9030753,4.80792317 C19.1748098,4.61074432 19.7454524,4.31002402 19.9934102,4.23079232 C20.2405188,4.15246097 20.2906198,4.07412967 20.3279833,3.86434575 C20.3653469,3.65366147 20.2532564,3.55012006 20.6498189,3.53571428 C21.0455324,3.5240096 21.0710075,3.36644659 21.2561267,3.07833133 C21.442095,2.78931573 21.6773152,2.40936375 21.6289125,1.89975991 C21.5788114,1.38835535 21.4047315,1.15246099 21.5541855,0.864345741 C21.7027903,0.575330135 21.9507481,0.234994 22,0 L22,0 Z" id="形状结合" fill="#0F1A3F"/>
                    <path d="M48.2598889,0.0598978975 C48.2660336,0.21962562 48.3020246,0.666364094 48.3222146,0.75205136 C48.3441604,0.83940246 48.3862961,1.03157487 48.295002,1.11143873 C48.2037079,1.1913026 48.1685948,1.32524095 48.0834458,1.53737933 C47.9982964,1.75034963 47.9350928,2.1696349 48.1185589,2.3826052 C48.3020246,2.59474358 48.6821235,2.90088838 48.7602503,3.25362044 C48.8374991,3.60635247 48.8515444,3.8201547 48.8655896,3.95242922 C48.8805128,4.08553565 48.8875354,4.39168044 49.3317158,4.62461671 C49.7758961,4.85755298 50.5027369,5.16369777 50.594031,5.70277883 C50.686203,6.24102798 50.7415061,6.66031328 50.8758135,6.69358988 C51.0101209,6.72686648 51.2067544,6.7335218 51.3138491,6.83335163 C51.4191885,6.93318147 51.5605185,6.99307935 51.608799,6.99973467 C51.6588353,7.00638999 51.6377674,6.88659419 51.6307448,6.78094095 C51.6237221,6.67362392 51.6026543,5.94153851 51.5956316,5.78181079 C51.588609,5.62291499 51.5254054,5.17118501 51.7158941,4.79765507 C51.9055046,4.42495708 52.2022103,3.8592547 52.3997213,3.72614827 C52.5963544,3.59304183 52.7447074,3.45328008 52.8500464,3.28024172 C52.9571415,3.10720335 53.4355571,2.08311571 53.4855934,1.96331992 C53.5356296,1.84435605 53.9929774,0.845225865 53.9859548,0.685498142 C53.97981,0.526602335 53.9508416,0.159727722 54,0" id="Stroke-5" fill="#0F1A3F"/>
                    <path d="M53,9 L50,13 M48,17 C47.8537879,17.2051635 47.2628788,18.2516351 46.9393939,18.8010327 C46.6151516,19.3504303 46,21 46,21 M15,39 C15,39.8888152 14.9549444,40.2190601 14.842598,40.9298478 C14.7302516,41.6406353 14.438268,42.2508271 14.438268,42.2508271 C14.438268,42.2508271 14.2165009,42.6863005 14,43 M14,30 C13.9768051,29.9107235 13.7433595,29.1239758 13.7164235,29.0360943 C13.4141414,28.0784656 12.8267864,27.1843069 12.4264871,26.6486487 C12.3928171,26.603313 12.0306771,26.0404533 12,26 M46.036267,26 L45.963733,30 M46,39 C45.7995595,39.4981963 45.2841412,40.6327092 45.0903086,41.0491326 C44.9331866,41.3885931 44.3039647,42.4763788 44,43 M8.06805495,54 C8.05467913,54.0787252 8.0387555,54.1649482 8.02219495,54.2605435 C7.84448744,55.2739768 7.8438505,56.0431115 7.8438505,56.0431115 C7.8438505,56.0431115 7.75531521,57.2833487 8.16423358,58 M37,52 L34,55 M24,66 C23.3096447,66.3320735 22.1378746,66.6738661 22.1378746,66.6738661 C22.1378746,66.6738661 21.1547405,66.9227862 20,67" id="Stroke-15" stroke="#075FC9" stroke-width="1.5"/>
                    <polygon id="Stroke-19" stroke="#0E5490" points="4.84661947 0 54 0 54 77 0 77 0 0.00743530252"/>
                </g>
            </g>
        </g>`
      })
      _this = null
    } else {
      _this = null
    }
    return this
  },
  flagGoe: function (code) { // 判断地图上下是否过长
    if (code == '210100' || code == '230000' || code == '654300' || code == '650100' || code == '230600' || code == '152200' || code == '150700' || code == '120000' || code == '659004' || code == '659001' || code == '150300') {
      return true
    } else {
      return false
    }
  },
  update: function (neSwitch) {
    this.enterLinks(this.nodeLine)
    this.enterNodes(neSwitch)
    this.updateNode().updateNodeImg().updateNodeRect()
    this.updateLink()
    this.nodeTick()
  },
  updateNodeImg: function (selection) {
    var _this = this
    var newthis = this
    var nodes = selection || this.china.selectAll('.node')
    var iconArr = []
    var topoIcons = mapTopology.prototype.iconsCollections
    nodes.each(function (d) {
      var icon = d.iconId + '_' + (d.runStatus || 'Loading')
      d3.select(this).select('.nodeImg').attr({
        'width': d.width,
        'height': d.height,
        'id': d.id
      })
      if (topoIcons) {
        d.iconId && iconArr.indexOf(icon) == -1 && !topoIcons[icon] && iconArr.push(icon)
      } else {
        iconArr.push(icon)
      }
    })
    if (iconArr.length) {
      getTopoIcon({iconInfo: JSON.stringify(iconArr)}, function (res) {
        var resObj = res.obj
        mapTopology.prototype.iconsCollections = $.extend(resObj, mapTopology.prototype.iconsCollections)
        console.log(newthis)
        newthis.setNodeImg(nodes)
      })
    } else {
      this.setNodeImg(nodes)
    }
    return this
  },
  updateNode: function () { // 隐藏网元
    var nodes = this.china.selectAll('.node')
    nodes.classed('hide', function (d) {
      return !d.show
    })
    return this
  },
  updateNodeRect: function (isSelected) {
    var nodes = isSelected ? this.china.selectAll('.node.selected') : this.china.selectAll('.node')
    nodes.each(function (d) {
      var node = d3.select(this)
      var rect = node.select('.nodeRect').attr('width', 0).attr('height', 0)
      var isHide = node.classed('hide')
      isHide && node.classed('hide', false) // getBBox方法在firefox不能用于隐藏的元素
      var box = this.getBBox()
      rect.attr({
        'width': box.width,
        'height': box.height
      })
      d.outerWidth = box.width
      d.outerHeight = box.height
      isHide && node.classed('hide', true)
      // node = rect = $img = $text = box = null
      node = rect = box = null
    })
    nodes = null
    return this
  },
  setNodeImg: function (nodes) {
    var icons = mapTopology.prototype.iconsCollections
    nodes.each(function (d) {
      var icon = d.iconId + '_' + (d.runStatus || 'Loading')
      if (d.iconId && icons[icon]) {
        d3.select(this).select('.nodeImg').attr({
          'href': 'data:image/png;base64,' + icons[icon]
        })
      }
    })
  },
  enterEnableLink: function () {
    var _this = this
    this.subLinks = []
    this.netLinks = this.links.filter(function (d, i) { // 子网的networkLinkId
      // == ''
      if (d.networkLinkId === '') {
        d.linkStatus = 'Enable'
        _this.subLinks.push(d)
      }
      if ((d.linkStatus == 'Enable' || d.linkStatus == 'Alert') && d.show) {
        return d.networkLinkId
      }
    })
    var use = this.nodeLine.selectAll('use').data(this.netLinks.concat(this.subLinks), function (d) {
      return d.id
    })

    use.enter().insert('svg:use', 'g.node').attr({
      'width': 2,
      'height': 2,
      'x': 0,
      'y': -10,
      'href': '#liquid'
    }).attr('id', function (d) {
      return d.id
    }).append('animateMotion').attr({
      dur: '3s',
      rotate: 'auto',
      repeatCount: 'indefinite'
    })
    use.exit().remove()
    this.enterEnableText().updateEnableText()
    _this = null
    return this
  },
  enterEnableText: function () {
    var _this = this
    var text = this.nodeLine.selectAll('text').data(this.netLinks, function (d) {
      return d.id
    })

    text.enter().insert('svg:text', 'g.node').classed('pathText', true).attr('dy', function (d) {
      return (d.linkIndex == -1) ? '-6' : '.71em'
    }).append('textPath').attr('xlink:href', function (d) {
      return '#' + d.id
    })
    text.exit().remove()
    _this = text = null
    return this
  },
  updateEnableText: function (selection) {
    var _this = this
    selection = selection || this.nodeLine.selectAll('textPath')
    selection.text(
      function (d) {
        var label = d.label || _this.config.label
        if (label == 'BandwidthUsage') {
          return addUnit(d.speedUsage, '%') || '--'
        }
        if (label == 'Traffic') {
          var str = bytesToSize(d.upBps === null ? undefined : d.upBps) ? '↑ ' + bytesToSize(d.upBps) + 'ps ↓' +
            bytesToSize(d.downBps) + 'ps' : '↑-- ↓--'
          return str
        }
        return ''
      }).attr('startOffset', '55%')
    _this = null
    return this
  },
  nodeTick: function () { // 拖动时设置
    var _this = this
    this.china.selectAll('.node').each(function (d) {
      d.px = d.x
      d.py = d.y
      d3.select(this).attr({
        x: d.x,
        y: d.y
      })
    })
    this.lineTick()
    _this = null
  },
  enterNodes: function (neSwitch) {
    var _this = this
    var nodes = this.nodes
    if (neSwitch) {
      this.china.selectAll('.node').remove()
    }
    var node = this.china.selectAll('.node').data(nodes, function (d) {
      return d.id
    })
    this.updateNodeData()
    var svg = node.enter().append('svg:svg').classed('node', true)
    if (_this.iconChange) {
      svg.append('svg:rect').classed('nodeRect', true)
      svg.append('svg:image').classed('nodeImg', true).on('mouseenter', function (d) {
        if (!_this.showTip || (d.nodeType !== 'NE' && d.nodeType !== 'SubnetTopo')) { // 子网
          return
        }
        _this.tip.text(_this.nodeTip(d)).classed('hide', false)
        _this.tipRange(d3.event.pageX + 20, d3.event.pageY + 20)
      }).on('mouseleave', function (d) {
        _this.tip.classed('hide', true)
      })
    } else {
      svg.append('svg:circle').classed('sportFour', true)
        .attr('r', '10')
        .attr('cx', '10')
        .attr('cy', '10')
      svg.append('svg:circle').classed('sport', true)
        .attr('r', '4')
        .attr('stroke-width', '1')
        .attr('cx', '10')
        .attr('cy', '10').on('mouseenter', function (d) {
          if (!_this.showTip || (d.nodeType !== 'NE' && d.nodeType !== 'SubnetTopo')) { // 子网
            return
          }
          _this.tip.text(_this.nodeTip(d)).classed('hide', false)
          _this.tipRange(d3.event.pageX + 20, d3.event.pageY + 20)
        }).on('mouseleave', function (d) {
          _this.tip.classed('hide', true)
        })
      svg.append('svg:circle').classed('sportOne', true)
        .attr('r', '4')
        .attr('cx', '10')
        .attr('cy', '10')
      svg.append('svg:circle').classed('sportTwo', true)
        .attr('r', '4')
        .attr('cx', '10')
        .attr('cy', '10')
    }
    node.exit().remove()
    return this
  },
  updateNodeData: function (selection) {
    selection = selection || this.china.selectAll('.node')
    selection.each(function (d) { // 刷新其子元素绑定的数据，暂未有更好的方法
      d3.select(this).selectAll('.nodeImg').each(function () {
        this.__data__ = d
      })
    })
    // seletion = null
    selection = null
    return this
  },
  nodeTip: function (d) { // TODO 添加异步请求数据显示
    d.alertLevel = null
    d.alertLevelText = ''
    var label = [{
      name: '名称',
      key: 'name'
    }, {
      name: 'IP地址',
      key: 'ip'
    }, {
      name: '设备类型',
      key: 'neClassText'
    }, {
      name: '厂商',
      key: 'vendor'
    }, {
      name: '设备状态',
      key: 'runStatusText'
    }, {
      name: 'CPU利用率',
      key: 'cpuAvg',
      unit: '%'
    }, {
      name: '内存利用率',
      key: 'memoryAvg',
      unit: '%'
    }, {
      name: '告警等级',
      key: 'alertLevelText'
    }]
    if (d.nodeType == 'NE') {
      var indicatorNames = []
      if (d.baseNeClass == 'host') {
        indicatorNames = ['cpu_usage_avg', 'physical_memory']
      } else if (d.baseNeClass == 'network') {
        indicatorNames = ['network_cpu', 'network_memory']
      }
      newAjax({
        url: gbs.host + '/monitor/ne/view/' + d.neId,
        dataType: 'json',
        data: {
          indicatorNames: indicatorNames.length > 0
            ? indicatorNames.join(',')
            : ''
        },
        type: 'post',
        async: false,
        success: function (res) {
          if (res.success == true) {
            var data = res.obj
            d.alertLevel = data.alertLevel || d.alertLevel
            if (data.indicators) {
              $
                .each(
                  data.indicators,
                  function (idx, idc) {
                    if (!idc.indicatorValue && !idc.value) {
                      return
                    }
                    if (idc.indicatorName == 'physical_memory') {
                      if (Array.isArray(idc.indicatorValue)) {
                        idc.indicatorValue.length > 0 &&
                          (d.memoryAvg = idc.indicatorValue[0].memory_usage)
                      } else {
                        d.memoryAvg = idc.indicatorValue.memory_usage
                      }
                    } else if (idc.indicatorName == 'cpu_usage_avg') {
                      if (Array.isArray(idc.indicatorValue)) {
                        idc.indicatorValue.length > 0 &&
                          (d.cpuAvg = idc.indicatorValue[0].result)
                      } else {
                        d.cpuAvg = idc.indicatorValue.result
                      }
                    } else if (idc.indicatorName == 'network_memory') {
                      if (Array.isArray(idc.indicatorValue)) {
                        idc.indicatorValue.length > 0 &&
                          (d.memoryAvg = idc.indicatorValue[0].memory_usage)
                      } else {
                        d.memoryAvg = idc.indicatorValue.memory_usage
                      }
                    } else if (idc.indicatorName == 'network_cpu') {
                      if (Array.isArray(idc.indicatorValue)) {
                        idc.indicatorValue.length > 0 &&
                          (d.cpuAvg = idc.indicatorValue[0].used_cpu_usage)
                      } else {
                        d.cpuAvg = idc.indicatorValue.used_cpu_usage
                      }
                    } else if (idc.text == '内存配额使用率') {
                      d.memoryAvg = idc.value
                    } else if (idc.text == 'CPU配额使用率') {
                      d.cpuAvg = idc.value
                    }
                  })
            }
          }
        }
      })
    } else if (d.nodeType == 'SubnetTopo' && d.runStatus == 'Warning') {
      newAjax({
        url: gbs.host + '/monitor/topo/view/' + d.neId,
        dataType: 'json',
        type: 'post',
        async: false,
        success: function (res) {
          if (res.success == true) {
            var data = res.obj
            d.alertLevel = data.alertLevel || d.alertLevel
            d.alertLevelText = data.alertLevelText || d.alertLevelText
          }
        }
      })
    }
    var str = ''

    $.each(label, function (i, o) {
      if (d.baseNeClass == 'storage' && (o.key == 'cpuAvg' || o.key == 'memoryAvg')) {
        return true
      }
      var unit = [o.unit] || ''
      var v = addUnit(d[o.key], unit)
      // if (o.key == 'alertLevel') {
      //   v = levelMapName[parseInt(v)]
      // }
      v = ((v || v === 0) ? v : '--')
      if (d.runStatus == 'Unknow' &&
        (o.key == 'cpuAvg' || o.key == 'memoryAvg' || o.key == 'alertLevelText')) { // 资源状态为未知，不展示cpu、内存、告警
        v = '--'
      }
      str += (o.name + '：' + v + '\n')
    })
    return str
  },
  linkTip: function (d) {
    d.alertLevel = null
    if (d.linkStatus == 'Alert' || d.linkStatus == 'Unconnection') {
      newAjax({
        url: gbs.host + '/monitor/topo/linkView/' + d.networkLinkId,
        dataType: 'json',
        type: 'post',
        async: false,
        success: function (res) {
          if (res.success == true) {
            var data = res.obj
            d.alertLevel = data.alertLevel || d.alertLevel
            d.alertLevelText = data.alertLevelText || d.alertLevelText
          }
        }
      })
    }
    var label = [{
      name: '源端',
      key: 'source'
    }, {
      name: '源端接口',
      key: 'sourceIfName'
    }, {
      name: '目的端',
      key: 'target'
    }, {
      name: '目的端接口',
      key: 'targetIfName'
    }, {
      name: '状态',
      key: 'linkStatusText'
    }, {
      name: '上行流量',
      key: 'upBps'
    }, {
      name: '下行流量',
      key: 'downBps'
    }, {
      name: '带宽利用率',
      key: 'speedUsage',
      unit: '%'
    }, {
      name: '告警等级',
      key: 'alertLevelText'
    }]
    var str = ''
    $.each(label, function (i, o) {
      var unit = [o.unit] || ''
      if (o.key == 'upBps' || o.key == 'downBps') {
        var v = bytesToSize(d[o.key] === null ? undefined : d[o.key]) ? bytesToSize(d[o.key]) + 'PS' : '--'
      } else {
        var v = addUnit(d[o.key], unit)
      }

      // if (o.key == 'alertLevel') {
      //   v = levelMapName[parseInt(v)]
      // }
      if (o.key == 'source' || o.key == 'target') {
        if (d[o.key].nodeType == 'SubnetTopo') {
          v = d[o.key + 'NeName'] + '[' + d[o.key + 'Ip'] + ']'
        } else {
          v = d[o.key].name + '[' + d[o.key].ip + ']'
        }
      }

      v = ((v || v === 0) ? v : '--')
      str += (o.name + '：' + v + '\n')
    })
    return str
  },
  setForce: function (flag) {
    flag ? this.force.stop() : this.force.start()
    this.force.nodes().forEach(function (d, i) {
      d.fixed = flag
    })
  },
  enterLinks: function (parent) {
    var _this = this
    var linktree = this.links
    var linkCopy = this.nodeLine.selectAll('path.linkId').data(linktree, function (d) {
      return d.id
    })
    var link = this.nodeLine.selectAll('path.linkR').data(linktree, function (d) {
      return d.id
    })
    link.attr('status', function (d) {
      var oldStatus = d3.select(this).attr('status')
      if (oldStatus) {
        d3.select(this).classed(d.linkStatus, false)
      }
      d3.select(this).classed(d.linkStatus, true)
      return d.linkStatus
    })
    linkCopy.enter().insert('svg:path', 'g.node').attr('class', 'link linkId').attr('linkId', function (d) {
      return d.id
    }).on('mouseenter', function (d) {
      // $(mapTp.ele).find('#' + d.id).attr('stroke-width', '8px')
      $(_this.ele).find('#' + d.id).attr('stroke-width', '8px')
    }).on('mouseleave', function (d) {
      // $(mapTp.ele).find('#' + d.id).attr('stroke-width', d.linkClass['stroke-width'])
      $(_this.ele).find('#' + d.id).attr('stroke-width', d.linkClass['stroke-width'])
    })
    link.enter().insert('svg:path', 'g.node').attr('class', 'link linkR').attr('id', function (d) {
      return d.id
    }).on('mouseenter', function (d) {
      if (_this.showTip && d.networkLinkId) {
        _this.tip.html(_this.linkTip(d)).classed('hide', false)
        _this.tipRange(d3.event.pageX + 20, d3.event.pageY + 20)
      }
    }).on('mouseleave', function (d) {
      _this.tip.classed('hide', true)
    })
    link.exit().remove()
    linkCopy.exit().remove()
    this.enterEnableLink()
    return this
  },
  tipRange: function (x, y) { // tip范围确定
    var tip = this.tip.node().getBoundingClientRect()
    var maxX = window.innerWidth, maxY = window.innerHeight

    x = (x + tip.width) > maxX ? (maxX - tip.width - 100) : x
    y = (y + tip.height) > maxY ? (maxY - tip.height) : y
    this.tip.style({
      left: x + 'px',
      top: y + 'px'
    })
  },
  cancelSelected: function () {
    this.china.selectAll('.selected').classed('selected', false)
  },
  hideTip: function () { // 隐藏提示
    this.tip.classed('hide', true)
  },
  addNode: function (node, x, y) { // 增加节点
    var index = this.findNodeIndex(node.id)
    node = $.extend({}, this.config, node)
    if (node.status == 'DELETED' && index != -1) {
      this.deleteNodes(node)
      return
    }
    if (index == -1 && node.status !== 'DELETED') {
      node.fixed = true
      node.x = x - 7
      node.y = y - 10
      this.nodes.push(node)
    } else {
      this.nodes[index] = $.extend({}, this.nodes[index], node)
    }
  },
  addNodes: function (nodes) { // 增加多个节点
    var _this = this
    var p
    if (Object.prototype.toString.call(nodes) == '[object Array]') {
      if (nodes == false) {
        $.each(_this.provinces[0], function (i, d) {
          d3.select(this).each(function (d) {
            d.isBind = false
            d.neId = ''
          })
        })
      } else {
        $.each(nodes, function (i, j) {
          delete j.x
          delete j.y
          $.each(_this.provinces[0], function (k, l) {
            if (j.status == 'DELETED') {
              d3.select(this).each(function (d) {
                d.isBind = false
                d.neId = ''
              })
              _this.addNode(j, j.x, j.y)
            } else {
              if (l.__data__.properties.adcode == parseInt(j.locationCode.substring(j.locationCode.length - 6))) {
                d3.select(this)
                  .each(function (d) {
                    p = _this.path.centroid(d.geometry)
                    d.isBind = true
                    d.neId = j.neId
                  })
                if (l.__data__.properties.adcode == 620000) {
                  _this.addNode(j, p[0], p[1] - 10)
                } else if (l.__data__.properties.adcode == 130000) {
                  _this.addNode(j, p[0] - 15, p[1])
                } else {
                  _this.addNode(j, p[0], p[1])
                }
              }
            }
          })
        })
      }
    }
    _this = null
    return this
  },
  deleteNodes: function (nodes) { // 删除节点及其相关连线
    var _this = this
    var nodesArr = this.nodes
    var linksArr = this.links
    if (_.isPlainObject(nodes)) {
      nodes = [nodes]
    }
    if (_.isArray(nodes)) {
      $.each(nodes, function (i, d) {
        _.remove(nodesArr, function (n) {
          return d.id == n.id
        })
        _.remove(linksArr, function (n) {
          return d.id == n.sourceNodeId || d.id == n.targetNodeId
        })
      })
    }
    return this
  },
  findNodeIndex: function (id) { // 查找节点所在序号
    var nodes = this.nodes
    for (var i in nodes) {
      if (nodes[i]['id'] == id) { return i }
    }
    return -1
  },
  addLink: function (source, target, link) { // 增加连线
    var oldLink = _.find(this.links, ['id', link.id])
    if (oldLink) {
      oldLink.handSpeed = null
      delete oldLink.downBps
      delete oldLink.upBps
      delete oldLink.speedUsage
      if (oldLink.sourceId == link.targetId) {
        var oldSourseValue = oldLink.sourceIp
        oldLink.sourceIp = oldLink.targetIp
        oldLink.targetIp = oldSourseValue
      }
    }
    var o = this.changeLinkData(source, target, link, this.tpId)
    if (oldLink) {
      o = $.extend(true, _.cloneDeep(oldLink), o)
      _.remove(this.links, ['id', link.id])
    }
    return o
  },
  addLinks: function (links) {
    this.enterLinks()
    if (Object.prototype.toString.call(links) == '[object Array]') {
      var _this = this, newLinks = []
      links.forEach(function (link) {
        var s = link['sourceNodeId'], t = link['targetNodeId']
        if (s && t) {
          newLinks.push(_this.addLink(s, t, link))
        }
      })
      this.enterLinks() // 销毁删掉数据的dom
      this.links = this.links.concat(newLinks)
      this.enterLinks() // 对新的数据创建dom
      this.updateLink().lineTick()
    }
    _this = null
    return this
  },
  changeLinkData: function (source, target, link, tpId) {
    if (source && target) {
      var s = this.findNode(source) || source
      var t = this.findNode(target) || target
      return $.extend({}, link, {
        source: s,
        target: t,
        // topoId: baseData.mapTopo.tpId,
        topoId: tpId,
        sourceIp: s.ip,
        sourceNeId: s.neId,
        targetIp: t.ip,
        targetNeId: t.neId,
        sourceNodeId: s.id,
        targetNodeId: t.id,
        show: s.show && t.show
      })
    }
  },
  findNode: function (id) { // 查找节点
    var nodes = this.nodes
    for (var i in nodes) {
      if (nodes[i]['id'] == id) { return nodes[i] }
    }
    return null
  },
  updateLink: function (isSelected) {
    var _this = this
    var links = isSelected ? this.china.selectAll('.link.selected') : this.china.selectAll('.link')
    links.classed('hide', function (d) {
      var obj = {
        'stroke-width': d.lineWidth || _this.config.lineWidth,
        'stroke-dasharray': (d.linkClass && d.linkClass['stroke-dasharray']) || '0'
      }
      d.linkClass = $.extend(d.linkClass, obj)
      if ($(this).attr('id')) {
        d3.select(this).attr(d.linkClass).attr('class', 'link linkR ' + d.linkStatus)
      } else {
        d3.select(this).attr(d.linkClass).attr('class', 'link linkId')
      }
      return !d.show
    })
    _this = links = null
    return this
  },
  lineTick: function () {
    var _this = this
    _this.nodeLine
      .selectAll('.link')
      .each(
        function (d) {
          var s = _.filter(_this.nodes, {
            id: d.sourceNodeId
          })[0]
          var t = _.filter(_this.nodes, {
            id: d.targetNodeId
          })[0]
          if (s &&
            t &&
            ((!_this.dragNodeId && !_this.dragLinkId) ||
              _this.dragNodeId.indexOf(s.id) !== -1 ||
              _this.dragNodeId.indexOf(t.id) !== -1 || _this.dragLinkId == d.id)) {
            d = $.extend(d, _this.changeLinkData(s, t, d, _this.tpId))
            this.__data__ = d
            var path = _this.polygonalLine(s, t, d)
            d3.select(this).attr('d', path)
          }
        })

    _this.nodeLine.selectAll('use').each(
      function (d) {
        if ((!_this.dragNodeId && !_this.dragLinkId) ||
          _this.dragNodeId.indexOf(d.sourceNodeId) !== -1 ||
          _this.dragNodeId.indexOf(d.targetNodeId) !== -1 ||
          _this.dragLinkId == d.id) {
          var id = d3.select(this).attr('id')
          var path = _this.nodeLine.selectAll('.link[id="' + id + '"]').attr('d')
          d3.select(this).select('animateMotion').attr('path', path)
        }
      })

    _this.nodeLine.selectAll('.lineWrap').each(
      function (d) {
        if ((!_this.dragNodeId && !_this.dragLinkId) ||
          _this.dragNodeId.indexOf(d.sourceNodeId) !== -1 ||
          _this.dragNodeId.indexOf(d.targetNodeId) !== -1 ||
          _this.dragLinkId == d.id) {
          d3.select(this).attr('class', 'lineWrap ' + d.linkClass.lineType)
        }
      })
    _this.nodeLine.selectAll('textPath').each(
      function (d) {
        d3.select(this.parentNode).attr('transform', '')
        if (d != undefined) {
          if (!_this.dragNodeId ||
            (_this.dragNodeId.indexOf(d.sourceNodeId) !== -1 || _this.dragNodeId
              .indexOf(d.targetNodeId) !== -1)) {
            if ((d.target.x - d.source.x) < 0) {
              var b_box = d3.select(this.parentNode).node().getBBox()
              var x = b_box.x + b_box.width / 2
              var y = b_box.y + b_box.height / 2
              d3.select(this.parentNode).attr('transform',
                'rotate(180, ' + x + ',' + y + ')')
            }
          }
        }
      })
    _this.updateEnableText()
    _this = null
  },
  polygonalLine: function (s, t, d) { // 折线
    var sx = s.x + s.width / 2
    var sy = s.y + s.height / 2
    var tx = t.x + t.width / 2
    var ty = t.y + t.height / 2
    var linkClass = d.linkClass
    var linkSum = this.findLinkSum(s, t)
    var l = Math.abs(Math.sqrt((s.x - t.x) * (s.x - t.x) + (s.y - t.y) * (s.y - t.y))) || 1
    sx = sx - ((s.x - t.x) / l * s.width * 0.5)
    sy = sy - ((s.y - t.y) / l * s.height * 0.5)
    tx = tx + ((s.x - t.x) / l * t.width * 0.5)
    ty = ty + ((s.y - t.y) / l * t.height * 0.5)
    if (linkSum > 1) {
      sx = sx + ((s.y - t.y) / l * 5 * d.linkIndex)
      sy = sy - ((s.x - t.x) / l * 5 * d.linkIndex)
      tx = tx + ((s.y - t.y) / l * 5 * d.linkIndex)
      ty = ty - ((s.x - t.x) / l * 5 * d.linkIndex)
    }
    if (linkClass && linkClass.lineType == 'arc') {
      var route = this.lineC([[sx, sy], [tx, ty]])
      var center = (sx + tx / 2, sy + ty / 2)
      var x = (sx + tx) / 2 - 20
      var y = (sy + ty) / 2 - 30
      if (sx < tx && sy < ty || sx > tx && sy > ty) {
        var x = (sx + tx) / 2 + 10
        var y = (sy + ty) / 2 - 20
      }
      var pathArr = route.split('L')
      var qPath = 'Q' + x + ',' + y + ' ' + pathArr[1].substr(0)
      return pathArr[0] + ' ' + qPath
    } else {
      return this.lineC([[sx, sy], [tx, ty]])
    }
  },
  findLinkByLinkId: function (id) {
    var index = _.findIndex(this.links, function (o) {
      return id == o.id
    })
    return index
  },
  findLinkIndex: function (sourceNode, targetNode) {
    var s0 = sourceNode.id, t0 = targetNode.id
    var index = _.findIndex(this.links, function (o) {
      var s = o.sourceNodeId, t = o.targetNodeId
      return (s0 == s && t0 == t) || (s0 == t && t0 == s)
    })
    return index
  },
  findLinkSum: function (sourceNode, targetNode) {
    var sum = 0
    var _links = this.links
    var s0 = sourceNode.id, t0 = targetNode.id
    $.each(_links, function (i, d) {
      var s = d.sourceNodeId, t = d.targetNodeId
      if ((s0 == s && t0 == t) || (s0 == t && t0 == s)) {
        sum++
      }
    })
    return sum
  },
  addNodeDrag: function () {
    this.china.selectAll('.node').on('mousedown.drag', null)
    return this
  },
  _addLink: function (callback) {
    var _this = this
    this.cancelNodeDrag()
    this.china.selectAll('.node').on('mousedown', null).on('mousedown', function (d) {
      // console.log('新增连线：node-mousedown')
      var downXY = d3.mouse(_this.china[0][0])
      _this.mousedownNode = d
      _this.dragline.lineData = [[downXY[0], downXY[1]]]
      _this.dragline.classed('hidden', false).attr('d', function (d) {
        return _this.lineC(_this.dragline.lineData)
      })
      d3.select(document).on('mousemove.tp', function () {
        // console.log('新增连线：node-mousemove')
        _this.cancelZoom()
        if (_this.mousedownNode == null) {
          return
        }
        _this.dragline.attr('d', function (d) {
          var xy = d3.mouse(_this.svgContainer.node())
          _this.dragline.lineData[1] = [xy[0], xy[1]]
          return _this.lineC(_this.dragline.lineData)
        })
      }).on('mouseup.tp', function () {
        var source = _this.mousedownNode
        var target = d3.event.target.__data__

        _this.dragline.classed('hidden', true)
        _this.dragline.lineData = []
        _this.mousedownNode = null
        d3.select(document).on('mousemove.tp', null).on('mouseup.tp', null)

        if (_this.canAddLinks(source, target)) {
          typeof callback === 'function' && callback([{
            // topoId: baseData.mapTopo.tpId,
            topoId: _this.tpId,
            sourceNodeId: source.id,
            targetNodeId: target.id
          }])
        }
      })
    })
  },
  createLiquid: function () {
    this.liquid = this.svgContainer.append('defs').append('g').attr('id', 'liquid').attr(
      'transform', 'translate(0,10)').html(
      '<path d="M-1,0 Q2,2 4,2 Q6,2 6,0 T4,-2 Q2,-2 -1,0" fill="#fff" ></path>')
  },
  cancelZoom: function () {
    this.svgContainer.classed('cursorMove', false).classed('cursorAuto', true)
    return this
  },
  canAddLinks: function (sor, tar) {
    // sor与tar不能为同一节点;节点间不能重复添加
    if (!sor || !tar) {
      return false
    }
    if (sor.nodeType && tar.nodeType) {
      if (sor.id == tar.id) {
        return false
      }
      return true
    }
    return false
  },
  cancelNodeDrag: function () {
    this.china.selectAll('.node').on('mousedown.drag', null)
    return this
  },
  destoryed: function () {
    this.cancelNodeDrag()
  }
}

// 给对应的数据加单位
function addUnit (value, unit, index) {
  if (typeof value === 'undefined' || value === 'undefined' || value === 'NaN' || value === 'null') {
    return '--'
  }
  if ((typeof value) === 'number' || !!Number(value)) {
    if (value % 1 != 0) {
      value = parseFloat(value).toFixed(2)
      value = value == '0.00' ? 0 : value
    }
  }
  if (!unit) {
    return value
  }
  switch (unit) {
    case 'timeStampToDate': // 时间戳转时间2017-10-19 15:27:32
      return value && Number(value * 1000) ? timestampformat(Number(value * 1000)) : '--'
    case 'bytesToSize':
      return bytesToSize(value, index)
    case 'duringTime': // 13天2时18分27秒
      return duringTime(value, index)
    default:
      if (value || value === 0) {
        unit = unit || ''
        return value + ' ' + unit
      } else {
        return '--'
      }
  }
}

function timestampformat (timestamp) {
  if (timestamp) {
    return (new Date(timestamp)).format('yyyy-MM-dd hh:mm:ss')
  }
}

// 保留两位小数，byte 转换 成其他
function bytesToSize (bytes, index) {
  if (bytes === 'undefined' || bytes === 'NaN' || bytes === 'null' || bytes === false) {
    return '--'
  }

  if (Number(bytes) !== 0 && !Number(bytes)) {
    return bytes
  }
  bytes = Number(bytes)
  var sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
  if (bytes > 0) {
    var k = 1024
    var i = Math.floor(Math.log(bytes) / Math.log(k))
    if (i < 0) {
      return bytes.toFixed(2) + ' ' + sizes[index || 0]
    }
    return (bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[index ? i + index : i]
  } else if (bytes === 0) {
    return '0 ' + sizes[index || 0]
  } else {
    return bytes
  }
}

function duringTime (t, unit) {
  var arr = ['天', '时', '分', '秒', '厘秒', '毫秒']
  var gap = [24, 60, 60, 100, 1000]
  var time = [null, null, null, null, null]
  unit = unit || '秒'
  var index = arr.indexOf(unit)
  if (t === 0) {
    return t + unit
  }
  for (var i = index; i >= 0; i--) {
    if (t <= 0) {
      break
    }
    if (i == 0) {
      time[0] = t + arr[i]
      break
    }
    time[i] = t % gap[i - 1] + arr[i]
    t = Math.floor(t / gap[i - 1])
  }

  return time.join('')
}
export default mapTopology
