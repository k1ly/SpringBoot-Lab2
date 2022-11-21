const path = require('path');

module.exports = [{
    mode: 'development',
    entry: {
        'login': './src/main/js/login.jsx',
        'register': './src/main/js/register.jsx',
        'home': './src/main/js/home.jsx',
        'admin': './src/main/js/admin.jsx'
    },
    output: {
        path: path.resolve(__dirname, 'src/main/resources/static/js'),
        filename: '[name].js'
    },
    devtool: 'inline-source-map',
    module: {
        rules: [
            {
                test: /\.jsx?$/,
                exclude: /(node_modules)/,
                loader: 'babel-loader',
                options: {
                    presets: ['@babel/preset-env', '@babel/preset-react']
                }
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            }
        ]
    }
},
    {
        mode: 'development',
        entry: {
            'login': './src/main/js/login.jsx',
            'register': './src/main/js/register.jsx',
            'home': './src/main/js/home.jsx',
            'admin': './src/main/js/admin.jsx'
        },
        output: {
            path: path.resolve(__dirname, 'target/classes/static/js'),
            filename: '[name].js'
        },
        devtool: 'inline-source-map',
        module: {
            rules: [
                {
                    test: /\.jsx?$/,
                    exclude: /(node_modules)/,
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env', '@babel/preset-react']
                    }
                },
                {
                    test: /\.css$/,
                    use: ['style-loader', 'css-loader']
                }
            ]
        }
    }
]

//npm run watch