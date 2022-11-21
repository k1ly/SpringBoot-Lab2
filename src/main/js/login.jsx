import React from "react";
import ReactDOM from "react-dom/client";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

class LoginComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {login: '', password: ''};
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        axios.post('/login', JSON.stringify({
            login: this.state.login,
            password: this.state.password
        }), {
            headers: {
                'Accept': 'text/plain',
                'Content-Type': 'application/json'
            }
        }).then(result => {
            sessionStorage.setItem('token', result.data);
            alert('Вход в систему выполнен!');
            window.location = '/';
        }).catch(error => {
            this.setState({error: 'Invalid username or password'});
            this.setState({login: '', password: ''});
            console.error('Error:', error.response ? error.response.data : error);
        })
    }

    render() {
        return <div className='position-absolute d-flex justify-content-center align-items-center'
                    style={{width: '-webkit-fill-available', height: '-webkit-fill-available'}}>
            <form className={'form-control'} style={{width: 'fit-content'}}>
                <div>
                    <div className={'text-center fw-semibold'}>Логин</div>
                    <label>
                        <input type='text' placeholder='Enter your login'
                               value={this.state.login}
                               className={'form-label'}
                               onChange={e => this.setState({login: e.target.value})}/>
                    </label>
                </div>
                <div>
                    <div className={'text-center fw-semibold'}>Пароль</div>
                    <label>
                        <input type='password' placeholder='Enter your password'
                               value={this.state.password}
                               className={'form-label'}
                               onChange={e => this.setState({password: e.target.value})}/>
                    </label>
                </div>
                <div
                    className={`${this.state.error ? 'd-block' : 'd-none'} text-danger fw-bold fst-italic red`}>{this.state.error}</div>
                <button className={'btn btn-primary w-100'} onClick={this.handleSubmit}>Войти</button>
            </form>
        </div>;
    }
}

ReactDOM.createRoot(document.getElementById("login")).render(<LoginComponent/>)