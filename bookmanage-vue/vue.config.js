module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: `http://127.0.0.1:8083/`,// 后端接口
        changOrigin: true, // 是否跨域
        pathRewrite: {
          '^/api':''
        },
      }
    },

    port: 8082, // 此处修改你想要的端口号
    host:'0.0.0.0'

  },

}
