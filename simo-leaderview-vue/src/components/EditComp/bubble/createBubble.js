import * as d3 from 'd3'

var colorSet = [
  '#6cc569',
  '#8a8939',
  '#6860ce',
  '#4a9aec',
  '#2773e5',
  '#004b72',
  '#ff8a23',
  '#2248d7',
  '#015c60',
  '#ae53bc',
  '#12ddef',
  '#0075a9',
  '#cc1a83',
  '#0e7e55',
  '#002dc3',
  '#f7ae68',
  '#08b8f2',
  '#c744b3',
  '#68d4f7',
  '#b6503c',
  '#f97960',
  '#4f8516',
  '#efe390',
  '#cc8241'
]

function cutWord(width, word) {
  let count = Math.round(width / 20)
  if (word.length <= count) {
    return word
  } else {
    return `${word.slice(0, count)}...`
  }
}

function createItem(options) {
  let circle = document.createElement('div')
  const { size, name, value, color, marginTop, marginLeft, floatDir } = options

  circle.style.display = 'block'
  circle.style.width = `${size}px`
  circle.style.height = `${size}px`
  circle.style.backgroundColor = color
  circle.style.borderRadius = '50%'
  circle.style.marginTop = `${marginTop}px`
  circle.style.marginLeft = `${marginLeft}px`
  circle.style.float = floatDir
  // circle.style.whiteSpace = 'nowrap'
  // circle.style.overflow = 'hidden'
  // circle.style.textOverflow = 'ellipsis'
  circle.style.padding = `${size * 0.1}px 0`
  circle.style.lineHeight = `${size * 0.4}px`
  circle.style.textAlign = 'center'
  circle.style.fontSize = '24px'
  const txt1 = cutWord(size - 8, name)
  const txt2 = cutWord(size - 8, String(value))
  circle.innerHTML = `<p class="text_overflow" style="padding: 0 4px; color: #fff">${txt1}</p><p class="text_overflow" style="color: #fff">${txt2}</p>`
  return circle
}

async function createBubble(width, height, chartData) {
  let bubbleBox = document.getElementById('bubbleBox')
  if (bubbleBox) {
    bubbleBox.remove()
  }
  let container = document.createElement('div')
  container.setAttribute('id', 'bubbleBox')
  container.classList.toggle('clear_float', true)
  // .setAttribute('id', 'bubbleDom')
  container.style.width = `${width}px`
  //   container.style.position = 'absolute'
  //   container.style.top = 0
  //   container.style.left = 0
  // container.style.visibility = 'hidden'

  // 辅助
  const extent = chartData.every(d => d.value == 0)
    ? [0, 60]
    : d3.extent(chartData, d => d.value)
  const scaleLinear = d3
    .scaleLinear()
    .domain(extent)
    .range([120, 160])

  chartData.map((item, index) => {
    const color = colorSet[index % colorSet.length]
    const size = scaleLinear(item.value)
    const floatDir = Math.round(Math.random() * 20) % 2 == 0 ? 'left' : 'right'
    const marginTop = Math.round(Math.random() * (220 - size))
    const marginLeft = Math.round(Math.random() * 40) + 20
    // console.log(marginTop, marginLeft)
    const options = {
      name: item.name,
      value: item.value,
      size,
      color,
      marginTop,
      marginLeft,
      floatDir
    }
    container.appendChild(createItem(options))
  })

  document.getElementById('app').appendChild(container)
  const canvasOptions = {
    // width,
    // height,
    logging: false,
    backgroundColor: null
  }
  return html2canvas(container, canvasOptions).then(resolve => {
    document.getElementById('bubbleBox').style.display = 'none'
    return resolve
  })
  //   .then(canvas => {
  //     document.getElementById('app').innerHTML = null
  //     document.getElementById('app').appendChild(canvas)
  //   })
}

export default createBubble
