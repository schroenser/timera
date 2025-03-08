const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const path = require('path');

module.exports = {
    mode: 'development',
    entry: './src/index.tsx',
    output: {
        path: path.resolve(__dirname, 'build'),
        filename: 'bundle.js'
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
                target: 'http://localhost:8080',
            },
        ]
    }
};
