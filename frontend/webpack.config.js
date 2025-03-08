const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const WrmPlugin = require('atlassian-webresource-webpack-plugin');
const path = require('path');

module.exports = {
    mode: 'development',
    entry: {
        'timera': './src/index.tsx'
    },
    output: {
        path: path.resolve(__dirname, 'build'),
        filename: 'bundled.[name].js'
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            }, {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/
            }]
    },
    resolve: {
        extensions: ['.tsx', '.ts', '.js']
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: 'public/index.html',
            favicon: 'public/timera.ico'
        }), new WrmPlugin({
            pluginKey: 'de.schroenser.timera.timera',
            xmlDescriptors: path.resolve('build', 'META-INF', 'plugin-descriptors', 'wr-defs.xml')
        })],
    devServer: {
        host: 'localhost',
        historyApiFallback: true,
        port: 3000,
        open: false,
        hot: true,
        proxy: [
            {
                context: ['/api'],
                target: 'http://localhost:8080'
            }]
    }
};
