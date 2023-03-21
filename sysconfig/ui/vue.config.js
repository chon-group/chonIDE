const isDev = process.env.NODE_ENV === 'dev';

const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    outputDir: "../resources/",
    publicPath: isDev ? "/" : "/chonide",
    pages: {
        login: {
            entry: "src/main.ts",
            filename: "login.html",
            template: "public/index.html",
            chunks: ['chunk-vendors', 'chunk-common', 'login']
        },
        manager: {
            entry: "src/main.ts",
            filename: "manager.html",
            template: "public/index.html",
            chunks: ['chunk-vendors', 'chunk-common', 'manager']
        },
        connect: {
            entry: "src/main.ts",
            filename: "connect.html",
            template: "public/index.html",
            chunks: ['chunk-vendors', 'chunk-common', 'connect']
        },
        domain: {
            entry: "src/main.ts",
            filename: "domain.html",
            template: "public/index.html",
            chunks: ['chunk-vendors', 'chunk-common', 'domain']
        },
        project: {
            entry: "src/main.ts",
            filename: "project.html",
            template: "public/index.html",
            chunks: ['chunk-vendors', 'chunk-common', 'project']
        },
        home: {
            entry: "src/main.ts",
            filename: "home.html",
            template: "public/index.html",
            chunks: ['chunk-vendors', 'chunk-common', 'home']
        }
    }
})
