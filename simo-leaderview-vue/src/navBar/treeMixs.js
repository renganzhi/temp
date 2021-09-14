export const treeFormatData = { // 数据转换
  data() {
    return {
      treeIdMap: {}
    }
  },
  methods: {
    transTreeData(nodes, setings, selectedIds) {
      nodes = JSON.parse(JSON.stringify(nodes))
      selectedIds = selectedIds || [] // 默认选中id,暂时仅处理了simple数据
      setings = {
        simpleData: true,
        id: 'id',
        pId: 'parentId',
        title: 'name',
        checked: 'checked',
        disabled: 'disabled',
        expand: true,
        parentNode: true,
        ...setings
      }
      setings.idKey = setings.idKey || 'id'

      const tem = {}
      nodes.forEach((d, i) => {
        d.uxIndex = i // 在数组的序号
        this.setSelected(selectedIds, d)
        for (let key in setings) {
          if (key !== 'simpleData') {
            d[key] = typeof setings[key] === 'string' ? d[setings[key]] : setings[key]
          }
        }
        tem[d[setings.idKey || 'idKey']] = d
      })
      if (setings.simpleData) {
        nodes = this.transChildData(nodes, setings, tem, selectedIds)
      }
      this.setTreeIdMap(nodes, setings.idKey)
      return nodes
    },
    setTreeIdMap(nodes, idKey) {
      nodes && nodes.length && nodes.forEach(d => {
        // 接口返回的数组中，父节点在子节点之后时，子节点的level为NaN的情况--重新设置level
        if (Object.prototype.toString.call(d.parentNode) === '[object Object]' && !d.level) d.level = d.parentNode.level + 1
        this.$set(this.treeIdMap, d[idKey], d)
        this.setTreeIdMap(d.children, idKey)
      })
    },
    transChildData(nodes, setings, tem, selectedIds) {
      const r = []
      nodes.forEach((d) => {
        this.setSelected(selectedIds, d)
        const p = tem[d[setings.pId]]
        if (p && d[setings.idKey] !== d[setings.pId]) {
          d.level = p.level + 1
          if (setings.parentNode) {
            d.parentNode = p
          }
          var children = this.children(p)
          if (!children) {
            children = this.children(p, [])
          }
          children.push(d)
        } else {
          d.level = 1
          r.push(d)
        }
      })
      return r
    },
    children: (node, newChildren) => {
      if (typeof newChildren !== 'undefined') {
        node.children = newChildren
      }
      return node.children
    },
    setSelected(selectedIds, d) {
      if (selectedIds.length && selectedIds.includes(d.id)) {
        d.selected = true
      }
    },
    // 角色start
    chgChecked(chkedArr, node) { // 点击触发事件，如角色新增；选中子节点即选中父节点，选中父节点选中其子节点
      if (node.checked) {
        this.chkedChildren(node)
        this.chkedParent(node)
      } else {
        this.unchkedParent(chkedArr, node)
        this.unchkedChildren(node, node.checked)
      }
    },
    chkedParent(node) {
      if (node.parentNode) {
        if (typeof node.parentNode !== 'boolean') { node.parentNode.checked = node.checked }
        this.chkedParent(node.parentNode)
      }
    },
    chkedChildren(node) {
      if (node.children) {
        node.children.forEach(d => {
          d.checked = node.checked
          this.chkedChildren(d)
        })
      }
    },
    unchkedParent(chkedArr, node) {
      if (!node.parentNode) {
        return
      }
      let checked = node.checked
      let chk = [] // 同级节点
      chkedArr.forEach((d, i) => {
        if (d.checked) {
          checked && chk.push(d)
          if (!checked && d.parentId === node.parentId) { // false
            chk.push(d)
            return false
          }
        }
      })
      if (checked && node.children.length === chk.length) {
        if (typeof node.parentNode !== 'boolean') { node.parentNode.checked = true }
      }
      if (!checked && !chk.length) {
        if (typeof node.parentNode !== 'boolean') { node.parentNode.checked = false }
      }
      this.unchkedParent(chkedArr, node.parentNode)
    },
    unchkedChildren(node, checked) {
      if (node.children) {
        node.children.forEach(d => {
          d.checked = checked
          this.unchkedChildren(d, checked)
        })
      }
    },
    chkedLevel(id, pId, level) {
      let pNode = this.treeIdMap[pId]
      if (pNode.level >= level) return false
      if (id) {
        let curNode = this.treeIdMap[id]
        let deep = { level: curNode.level }
        this.findDeepNode(curNode.children, deep)
        if (pNode.level + deep.level - curNode.level + 1 > level) return false
      }
      return true
    },
    findDeepNode(nodes, deep) {
      nodes && nodes.length && nodes.forEach(d => {
        deep.level = Math.max(d.level, deep.level)
        this.findDeepNode(d.children, deep)
      })
    },
    deleteNode(nodes = [], id, idKey = 'id') {
      return nodes.filter(d => {
        if (d[idKey] === id) {
          return false
        } else {
          d.children = this.deleteNode(d.children, id, idKey)
          return d
        }
      })
    }
    // end

    // 父子节点不同时选中，兄弟节点间无限制,资源添加至分组
  }
}

export const treeGpRender = {
  methods: {
    addEdit() { },
    del() { },
    renderContent(h, { root, node, data }) {
      if (data.name === '默认分组') {
        return h('span', { class: 'ivu-tree-title' }, data.title)
      }
      return h('span', {
        style: {
          display: 'inline-block'
        }
      }, [
        h('span', { class: 'ivu-tree-title' }, data.title),
        h('span', {
          style: {
            display: 'inline-block',
            float: 'right',
            marginLeft: '20px'
          }
        }, [
          h('a', {
            class: 'us-link',
            style: {
              display: 'inline-block',
              marginRight: '5px'
            },
            on: {
              click: () => { this.addEdit(data) }
            }
          }, '编辑'),
          h('a', {
            class: 'us-link',
            on: {
              click: () => { this.del(data) }
            }
          }, '删除')
        ])
      ])
    }
  }
}
