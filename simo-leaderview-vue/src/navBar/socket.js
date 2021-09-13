import SockJS from 'sockjs-client'
export default function socket(options) {
  let opt = {
    path: '/socket',
    ...options
  }
  let socket = new SockJS(opt.path)
  socket.onopen = () => {
    opt.pram && socket.send(opt.pram)
    opt.onopen && opt.onopen()
  }
  socket.onclose = () => {
    opt.onclose && opt.onclose()
  }
  socket.onmessage = (msg) => {
    opt.onmessage && opt.onmessage(msg)
  }
  socket.onerror = (msg) => {
    opt.onerror && opt.onerror(msg)
  }
  return socket
}
