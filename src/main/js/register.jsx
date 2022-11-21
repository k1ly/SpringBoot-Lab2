import React from "react";
import ReactDOM from "react-dom/client";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

class RegistrationComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {login: '', password: '', matchingPsw: '', name: '', email: ''};
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        axios.post('/api/users/add', JSON.stringify({
            login: this.state.login,
            password: this.state.password,
            matchingPsw: this.state.matchingPsw,
            name: this.state.name,
            email: this.state.email
        }), {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(result => {
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
                alert('Регистрация выполнена успешно!');
                window.location = '/';
            }).catch(error => {
                this.setState({error: 'Invalid username or password'});
                this.setState({login: '', password: '', matchingPsw: '', name: '', email: ''});
                console.error('Error:', error.response ? error.response.data : error);
            })
        }).catch((error) => {
            console.error('Error:', error.response ? error.response.data : error);
        })
    }

    render() {
        return <div className='position-absolute d-flex justify-content-center align-items-center'
                    style={{width: '-webkit-fill-available', height: '-webkit-fill-available'}}>
            <form className={'form-control'} style={{width: 'fit-content'}}>
                <div>
                    <div className={'text-center fw-semibold'}>Логин</div>
                    <div>
                        <input type='text' placeholder='Enter your login'
                               value={this.state.login}
                               className={'form-label'}
                               onChange={e => this.setState({login: e.target.value})}/>
                    </div>
                </div>
                <div>
                    <div className={'text-center fw-semibold'}>Пароль</div>
                    <div>
                        <input type='password' placeholder='Enter your password'
                               value={this.state.password}
                               className={'form-label'}
                               onChange={e => this.setState({password: e.target.value})}/>
                    </div>
                </div>
                <div>
                    <div className={'text-center fw-semibold'}>Повторите пароль</div>
                    <div>
                        <input type='password' placeholder='Repeat your password'
                               value={this.state.matchingPsw}
                               className={'form-label'}
                               onChange={e => this.setState({matchingPsw: e.target.value})}/>
                    </div>
                </div>
                <div>
                    <div className={'text-center fw-semibold'}>Имя</div>
                    <div>
                        <input type='text' placeholder='Enter your name'
                               value={this.state.name}
                               className={'form-label'}
                               onChange={e => this.setState({name: e.target.value})}/>
                    </div>
                </div>
                <div>
                    <div className={'text-center fw-semibold'}>Почта</div>
                    <div>
                        <input type='email' placeholder='Enter your email'
                               value={this.state.email}
                               className={'form-label'}
                               onChange={e => this.setState({email: e.target.value})}/>
                    </div>
                </div>
                <div
                    className={`${this.state.error ? 'd-block' : 'd-none'} text-danger fw-bold fst-italic red`}>{this.state.error}</div>
                <button className={'btn btn-primary w-100'} onClick={this.handleSubmit}>Зарегистрироваться</button>
            </form>
        </div>;
    }
}

ReactDOM.createRoot(document.getElementById("register")).render(<RegistrationComponent/>);
