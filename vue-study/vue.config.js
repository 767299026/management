const {chainWebpack} = require("./vue.config");

module.exports = {
    chainWebpack: config => {
        config.set('externals',{
            vue: 'Vue',
            'element-ui': 'ELEMENT',
            lodash: '_',
            echarts: 'echarts',
            'vue-quill-editor': 'VueQuillEditor'
        })
    },
    publicPath: './',
    transpileDependencies: true,

}

