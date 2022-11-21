import React from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

class UserListComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {page: {content: [], first: true, last: true, number: 0, size: 20, totalPages: 0}};
        this.loadUsersAsync = this.loadUsersAsync.bind(this);
        this.handleUser = this.handleUser.bind(this);
    }

    loadUsersAsync(page) {
        axios.get(`/api/users?page=${page.number}&size=${page.size}&sort=id,asc`, {
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
                'Accept': 'application/json'
            }
        }).then(result => {
            this.setState({page: result.data});
        }).catch(error => {
            console.error('Error:', error.response ? error.response.data : error);
            if (error.response.status === 401)
                location.href = '/login'
        })
    }

    handleUser(e) {
        this.setState({user: this.state.page.content.find(u => u.id === parseInt(e.currentTarget.closest('tr').dataset['id']))});
    }

    componentDidMount() {
        this.loadUsersAsync(this.state.page);
    }

    render() {
        return <div>
            {this.state.user ?
                <div
                    className={'position-absolute d-flex justify-content-center align-items-center top-0 vw-100 vh-100'}
                    style={{zIndex: 10, left: 0, background: 'rgba(0,0,0,0.4)'}}>
                    <div className={'p-4 rounded shadow'} style={{backgroundColor: 'white'}}>
                        <div style={{display: 'flex', justifyContent: 'right'}}>
                            <button className={'btn btn-close'} onClick={e => this.setState({user: null})}></button>
                        </div>
                        <table className={'w-100'}>
                            <tbody>
                            <tr>
                                <td className={'fw-bold fs-5'}>Имя: {this.state.user.name}</td>
                            </tr>
                            <tr>
                                <td className={'fw-semibold'}>ID: {this.state.user.id}</td>
                            </tr>
                            <tr>
                                <td className={'fst-italic'}>Почта: {this.state.user.email}</td>
                            </tr>
                            <tr>
                                <td style={{color: '#1b0381'}}>
                                    <div className={'mt-2'}>Роли</div>
                                    <ul className={'list-group'}>{this.state.user.roles.map(r =>
                                        <li className={'list-group-item'} key={r.id}>{r.name}</li>)}
                                    </ul>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                : null}
            <div className={'container'}>
                <div style={{background: 'white'}}>
                    <ul className={'pagination mt-3'}>
                        {this.state.page.number > 1 ? <li className={'page-item'}>
                            {this.state.page.number > 2 ? '...' : null}
                            <button className={'page-link'} onClick={e =>
                                this.loadUsersAsync(0)}>
                                1
                            </button>
                        </li> : null}
                        {!this.state.page.first ? <li className={'page-item'}>
                            <button className={'page-link'}
                                    onClick={e => this.loadUsersAsync(this.state.page.number - 1)}>
                                {this.state.page.number}
                            </button>
                        </li> : null}
                        <li className={'page-item active'}>
                            <button className={'page-link'}
                                    onClick={e => this.loadUsersAsync(this.state.page.number)}>
                                {this.state.page.number + 1}
                            </button>
                        </li>
                        {!this.state.page.last ? <li className={'page-item'}>
                            <button className={'page-link'}
                                    onClick={e => this.loadUsersAsync(this.state.page.number + 1)}>
                                {this.state.page.number + 2}
                            </button>
                        </li> : null}
                        {this.state.page.number < this.state.page.totalPages - 2 ? <li className={'page-item'}>
                            {this.state.page.number < this.state.page.totalPages - 3 ? '...' : null}
                            <button className={'page-link'} onClick={e =>
                                this.loadUsersAsync(this.state.page.totalPages - 1)}>
                                {this.state.page.totalPages}
                            </button>
                        </li> : null}
                    </ul>
                    <table className={'table-striped w-100'}>
                        <thead>
                        <tr>
                            <th scope={'col'}>ID</th>
                            <th scope={'col'}>Имя</th>
                            <th scope={'col'}>Почта</th>
                        </tr>
                        </thead>
                        <tbody className={'table-group-divider'}>
                        {this.state.page.content.map(u =>
                            <tr key={u.id} data-id={u.id}>
                                <td>{u.id}</td>
                                <td>{u.name}</td>
                                <td>{u.email}</td>
                                <td className={'d-flex'} style={{justifyContent: 'right'}}>
                                    <button className={'btn btn-outline-primary m-1'}
                                            onClick={this.handleUser}>
                                        Просмотр
                                    </button>
                                </td>
                            </tr>)}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>;
    }
}

export default UserListComponent;
