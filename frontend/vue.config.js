const { defineConfig } = require('@vue/cli-service');
const webpack = require('webpack'); // VUE_PROD_HYDRATION_MISMATCH_DETAILS 플래그를 추가

module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: JSON.stringify(true) // 여기서 true로 수정
      })
    ]
  }
})
