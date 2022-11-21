import React from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

class ApplicationListComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {page: {content: [], first: true, last: true, number: 0, size: 20, totalPages: 0}};
        this.loadApplicationsAsync = this.loadApplicationsAsync.bind(this);
        this.handleApplication = this.handleApplication.bind(this);
        this.handleConfirm = this.handleConfirm.bind(this);
        this.handleCancel = this.handleCancel.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
    }

    loadApplicationsAsync(page) {
        axios.get(`/api/applications?page=${page.number}&size=${page.size}&sort=id,asc`, {
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

    handleApplication(e) {
        this.setState({application: this.state.page.content.find(a => a.id === parseInt(e.currentTarget.closest('tr').dataset['id']))});
    }

    handleConfirm(e) {
        axios.put(`/api/applications/update/${this.state.application.id}`, JSON.stringify({
            active: false
        }), {
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(result => {
            axios.put(`/api/trips/update-members/${this.state.application.trip.id}`, JSON.stringify({
                originator: this.state.application.originator,
                companion: this.state.application.companion,
            }), {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }).then(result => {
                alert('Участники зарегистрированы');
            }).catch(error => {
                console.error('Error:', error.response ? error.response.data : error);
            })
            let application = this.state.application;
            application.active = false;
            this.setState({application: application});
            alert('Заявка одобрена!');
        }).catch(error => {
            console.error('Error:', error.response ? error.response.data : error);
        })
    }

    handleCancel(e) {
        axios.put(`/api/applications/update/${this.state.application.id}`, JSON.stringify({
            active: false,
            trip: {id: this.state.application.trip.id},
            originator: {id: this.state.application.originator.id},
            companion: {id: this.state.application.companion.id}
        }), {
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(result => {
            let application = this.state.application;
            application.active = false;
            this.setState({application: application});
            alert('Заявка отклонена');
        }).catch(error => {
            console.error('Error:', error.response ? error.response.data : error);
        })
    }

    handleDelete(e) {
        axios.delete(`/api/applications/delete/${this.state.application.id}`, {
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(result => {
            alert('Заявка удалена!');
        }).catch(error => {
            console.error('Error:', error.response ? error.response.data : error);
        })
    }

    componentDidMount() {
        this.loadApplicationsAsync(this.state.page);
    }

    render() {
        return <div>
            {this.state.application ?
                <div
                    className={'position-absolute d-flex justify-content-center align-items-center top-0 vw-100 vh-100'}
                    style={{zIndex: 10, left: 0, background: 'rgba(0,0,0,0.4)'}}>
                    <div className={'p-4 rounded shadow'} style={{backgroundColor: 'white'}}>
                        <div style={{display: 'flex', justifyContent: 'right'}}>
                            <button className={'btn btn-close'}
                                    onClick={e => this.setState({application: null})}></button>
                        </div>
                        <table className={'w-100'}>
                            <tbody>
                            <tr>
                                <td className={'fw-bold'}>ID: {this.state.application.id}</td>
                            </tr>
                            <tr>
                                <td className={'fw-semibold fst-italic'}>Поездка: {this.state.application.trip.title} ({this.state.application.trip.id})</td>
                            </tr>
                            <tr>
                                <td>Инициатор: {this.state.application.originator.name} ({this.state.application.originator.id})</td>
                            </tr>
                            <tr>
                                <td>Попутчик: {this.state.application.companion.name} ({this.state.application.companion.id})</td>
                            </tr>
                            <tr>
                                <td>
                                    {this.state.application.active ?
                                        <div className={'d-inline-flex justify-content-between'}>
                                            <button className={'btn btn-outline-primary btn-sm me-2'}
                                                    onClick={this.handleConfirm}>
                                                Подтвердить
                                            </button>
                                            <button className={'btn btn-outline-primary btn-sm me-2'}
                                                    onClick={this.handleCancel}>
                                                Отклонить
                                            </button>
                                        </div> :
                                        <div className={'d-inline-flex justify-content-center'}>
                                            <button className={'btn btn-outline-secondary btn-sm'}
                                                    onClick={this.handleDelete}>
                                                Удалить
                                            </button>
                                        </div>
                                    }
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
                                this.loadApplicationsAsync(0)}>
                                1
                            </button>
                        </li> : null}
                        {!this.state.page.first ? <li className={'page-item'}>
                            <button className={'page-link'}
                                    onClick={e => this.loadApplicationsAsync(this.state.page.number - 1)}>
                                {this.state.page.number}
                            </button>
                        </li> : null}
                        <li className={'page-item active'}>
                            <button className={'page-link'}
                                    onClick={e => this.loadApplicationsAsync(this.state.page.number)}>
                                {this.state.page.number + 1}
                            </button>
                        </li>
                        {!this.state.page.last ? <li className={'page-item'}>
                            <button className={'page-link'}
                                    onClick={e => this.loadApplicationsAsync(this.state.page.number + 1)}>
                                {this.state.page.number + 2}
                            </button>
                        </li> : null}
                        {this.state.page.number < this.state.page.totalPages - 2 ? <li className={'page-item'}>
                            {this.state.page.number < this.state.page.totalPages - 3 ? '...' : null}
                            <button className={'page-link'} onClick={e =>
                                this.loadApplicationsAsync(this.state.page.totalPages - 1)}>
                                {this.state.page.totalPages}
                            </button>
                        </li> : null}
                    </ul>
                    <table className={'w-100'}>
                        <thead>
                        <tr>
                            <th scope={'col'}>ID</th>
                            <th scope={'col'}>Поездка</th>
                            <th scope={'col'}>Инициатор</th>
                            <th scope={'col'}>Попутчик</th>
                        </tr>
                        </thead>
                        <tbody className={'table-group-divider'}>
                        {this.state.page.content.map(a =>
                            <tr key={a.id} data-id={a.id}>
                                <td>{a.id}</td>
                                <td>{a.trip.title}</td>
                                <td>{a.originator.name}</td>
                                <td>{a.companion.name}</td>
                                <td className={'d-flex'} style={{justifyContent: 'right'}}>
                                    <button className={'btn btn-outline-primary m-1'}
                                            onClick={this.handleApplication}>
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

export default ApplicationListComponent;
