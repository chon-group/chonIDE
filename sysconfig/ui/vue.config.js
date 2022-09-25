const isDev = process.env.NODE_ENV === 'dev';

const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    outputDir: "../resources/",
    publicPath: isDev ? "/" : "/sysconfig"
})
