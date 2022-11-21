import React from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

class TripInfoComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            page: {content: [], first: true, last: true, number: 0, size: 3, totalPages: 0},
            application: false
        };
        this.loadUsersAsync = this.loadUsersAsync.bind(this);
        this.handleSelectUser = this.handleSelectUser.bind(this);
        this.handleApplication = this.handleApplication.bind(this);
    }

    loadUsersAsync(page) {
        axios.get(`/api/users?page=${page.number}&size=${page.size}&sort=id,asc`, {
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
                'Accept': 'application/json'
            }
        }).then(result => {
            result.data.content = result.data.content.filter(u => u.roles.filter(r => r.name === 'ROLE_MANAGER' || r.name === 'ROLE_ADMIN').length === 0);
            this.setState({page: result.data});
        }).catch(error => {
            console.error('Error:', error.response ? error.response.data : error);
            if (error.response.status === 401)
                location.href = '/login'
        })
    }

    handleSelectUser(e) {
        this.setState({companion: this.state.page.content.find(user => user.id === parseInt(e.currentTarget.closest('li').dataset['id']))});
    }

    handleApplication(e) {
        axios.post('/api/applications/add',
            JSON.stringify({
                active: true,
                trip: {id: this.props.trip.id},
                originator: {id: this.props.user.id},
                companion: {id: this.state.companion.id}
            }), {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }).then(result => {
            alert('Заявка отправлена!');
        }).catch(error => {
            console.error('Error:', error.response ? error.response.data : error);
            if (error.response.status === 401)
                location.href = '/login'
        })
    }

    componentDidMount() {
        this.loadUsersAsync(this.state.page);
    }

    render() {
        return <div className={'p-4 rounded shadow'} style={{backgroundColor: 'white'}}>
            <div style={{display: 'flex', justifyContent: 'right'}}>
                <button className={'btn btn-close'} onClick={this.props.onClose}></button>
            </div>
            <div>
                {this.props.trip ? <table className={'w-100'}>
                    <tbody>
                    <tr>
                        <td className={' fw-bold fs-5 text-center'}>{this.props.trip.title}</td>
                    </tr>
                    {this.props.user.roles.filter(r => r.name === 'ROLE_ADMIN').length > 0 ?
                        <tr>
                            <td className={'fw-semibold'}>ID: {this.props.trip.id}</td>
                        </tr> : null}
                    <tr>
                        <td className={'fst-italic'}>{this.props.trip.description}</td>
                    </tr>
                    {this.state.application ? <tr>
                        <td className={'fw-semibold'} style={{color: '#1b0381'}}> {this.props.user.name} (Вы)</td>
                    </tr> : null}
                    </tbody>
                </table> : null}
            </div>
            <div className={'d-flex justify-content-center p-2'}>
                <button className={'btn btn-primary'} disabled={!this.props.user}
                        onClick={e => this.setState({application: true})}>
                    Подать заявку
                </button>
            </div>
            {this.state.application ?
                (this.props.user.roles.filter(r => r.name === 'ROLE_MANAGER' || r.name === 'ROLE_ADMIN').length === 0 ?
                    <div>
                        <h2 className={'fw-semibold'}>Выберите себе попутчика</h2>
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
                        <ul style={{padding: 0, listStyleType: 'none', backgroundColor: '#e7e7e7'}}>
                            {this.state.page.content.map(user =>
                                <li key={user.id} data-id={user.id}>
                                    <div className={'btn btn-outline-dark m-1'} style={{width: 'calc(100% - 0.5rem)'}}
                                         onClick={this.handleSelectUser}>{user.name}
                                    </div>
                                </li>)}
                        </ul>
                        {this.state.companion ? <div>
                            Ваш попутчик:
                            <span className={'p-1 fw-bold fs-5'}
                                  style={{color: 'blue'}}>{this.state.companion.name}</span>
                        </div> : null}
                        <button className={'btn btn-success w-100'} disabled={!this.props.user}
                                onClick={this.handleApplication}>ОК
                        </button>
                    </div>
                    : <div className={'fw-bold fst-italic text-danger'}>
                        Оставлять заявки разрешено только пользователям с ролью клиента.
                    </div>)
                : null}
            {this.props.user.roles.filter(r => r.name === 'ROLE_ADMIN').length > 0 ?
                <div className={'d-inline-flex justify-content-between'}>
                    <button className={'btn btn-secondary btn-sm me-2'} onClick={this.props.onEdit}>
                        Изменить
                    </button>
                    <button className={'btn btn-outline-secondary btn-sm ms-2'} onClick={this.props.onDelete}>
                        Удалить
                    </button>
                </div> : null}
        </div>;
    }
}

class TripFormComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {title: '', description: '', startDate: Date.now(), finishDate: Date.now()};
    }

    render() {
        return <div className={'p-4 rounded shadow'} style={{backgroundColor: 'white'}}>
            <div style={{display: 'flex', justifyContent: 'right'}}>
                <button className={'btn btn-close'} onClick={this.props.onClose}></button>
            </div>
            <form className={'form-control'}>
                <div>
                    <div className={'fw-semibold'}>Название</div>
                    <div>
                        <input type='text' placeholder='Enter trip title'
                               value={this.state.title}
                               className={'form-label'}
                               onChange={e => this.setState({title: e.target.value})}/>
                    </div>
                </div>
                <div>
                    <div className={'fw-semibold'}>Описание</div>
                    <div>
                        <input type='text' placeholder='Enter trip description'
                               value={this.state.description}
                               className={'form-label'}
                               onChange={e => this.setState({description: e.target.value})}/>
                    </div>
                </div>
                <div>
                    <div className={'fw-semibold'}>Дата начала</div>
                    <div>
                        <input type='datetime-local' placeholder='Enter start date'
                               value={this.state.startDate}
                               className={'form-label'}
                               onChange={e => this.setState({startDate: e.target.value})}/>
                    </div>
                </div>
                <div>
                    <div className={'fw-semibold'}>Дата окончания</div>
                    <div>
                        <input type='datetime-local' placeholder='Enter finish date'
                               value={this.state.finishDate}
                               className={'form-label'}
                               onChange={e => this.setState({finishDate: e.target.value})}/>
                    </div>
                </div>
                <div className='d-flex justify-content-center'>
                    {this.props.edit ?
                        <button className={'btn btn-primary w-100'}
                                onClick={e => this.props.onUpdate(this.state)}>Изменить</button> :
                        <button className={'btn btn-primary w-100'}
                                onClick={e => this.props.onAdd(this.state)}>Добавить</button>}
                </div>
            </form>
        </div>;
    }
}

class TripListComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            page: {content: [], first: true, last: true, number: 0, size: 10, totalPages: 0},
            query: ''
        };
        this.loadTripsAsync = this.loadTripsAsync.bind(this);
        this.handleInfo = this.handleInfo.bind(this);
        this.handleAdd = this.handleAdd.bind(this);
        this.handleUpdate = this.handleUpdate.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
    }

    loadTripsAsync(page, query) {
        axios.get(`/api/trips?page=${page.number}&size=${page.size}&sort=id,asc${query ? '&q=' + query : ''}`, {
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

    handleInfo(e) {
        this.setState({tripInfo: this.state.page.content.find(trip => trip.id === parseInt(e.currentTarget.closest('tr').dataset['id']))})
    }

    handleAdd(trip) {
        axios.post('/api/trips/add',
            JSON.stringify(trip), {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }).then(result => {
            alert('Поездка добавлена!');
        }).catch(error => {
            console.error('Error:', error.response ? error.response.data : error);
            if (error.response.status === 401)
                location.href = '/login'
        })
    }

    handleUpdate(trip) {
        axios.put(`/api/trips/update/${this.state.tripInfo.id}`,
            JSON.stringify(trip), {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }).then(result => {
            alert('Поездка изменена!');
        }).catch(error => {
            console.error('Error:', error.response ? error.response.data : error);
            if (error.response.status === 401)
                location.href = '/login'
        })
    }

    handleDelete() {
        axios.delete(`/api/trips/delete/${this.state.tripInfo.id}`, {
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(result => {
            alert('Поездка удалена!');
        }).catch(error => {
            console.error('Error:', error.response ? error.response.data : error);
            if (error.response.status === 401)
                location.href = '/login'
        })
    }

    componentDidMount() {
        axios.post('/login/user', null, {
            params: {
                token: sessionStorage.getItem('token')
            },
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(result => {
            this.setState({user: result.data});
        }).catch(error => {
            console.error('Error:', error.response ? error.response.data : error);
            if (error.response.status === 401)
                location.href = '/login'
        })
        this.loadTripsAsync(this.state.page);
    }

    render() {
        return <div>
            {this.state.tripInfo ?
                <div
                    className={'position-absolute d-flex justify-content-center align-items-center top-0 vw-100 vh-100'}
                    style={{zIndex: 10, left: 0, background: 'rgba(0,0,0,0.4)'}}>
                    <TripInfoComponent trip={this.state.tripInfo}
                                       user={this.state.user}
                                       onEdit={e => this.setState({
                                           trip: this.state.tripInfo,
                                           editTrip: true
                                       })}
                                       onDelete={this.handleDelete}
                                       onClose={e => this.setState({tripInfo: null})}/>
                </div>
                : null}
            {this.state.addTrip || this.state.editTrip ?
                <div
                    className={'position-absolute d-flex justify-content-center align-items-center top-0 vw-100 vh-100'}
                    style={{zIndex: 11, left: 0, background: 'rgba(0,0,0,0.4)'}}>
                    <TripFormComponent edit={this.state.editTrip && !this.state.addTrip}
                                       onAdd={this.handleAdd}
                                       onUpdate={this.handleUpdate}
                                       onClose={e => this.setState({addTrip: false, editTrip: false})}/>
                </div>
                : null
            }
            {this.state.page ?
                <div className={'container'}>
                    <div className={'d-flex justify-content-center mt-5'}>
                        <div>
                            <input type='text' placeholder='Search'
                                   value={this.state.query}
                                   onChange={e => this.setState({query: e.target.value})}/>
                            <button className={'btn btn-success btn-sm ms-1'}
                                    onClick={e => this.loadTripsAsync(0, this.state.query === '' ? null : this.state.query)}>
                                Искать
                            </button>
                        </div>
                    </div>
                    {this.state.user
                    && this.state.user.roles.filter(r => r.name === 'ROLE_ADMIN').length > 0 ?
                        <div>
                            <button className={'btn btn-secondary mt-2'}
                                    onClick={e => this.setState({addTrip: true})}>
                                Добавить поездку
                            </button>
                        </div>
                        : null}
                    <ul className={'pagination mt-3'}>
                        {this.state.page.number > 1 ? <li className={'page-item'}>
                            {this.state.page.number > 2 ? '...' : null}
                            <button className={'page-link'} onClick={e =>
                                this.loadTripsAsync(0, this.state.query === '' ? null : this.state.query)}>
                                1
                            </button>
                        </li> : null}
                        {!this.state.page.first ? <li className={'page-item'}>
                            <button className={'page-link'}
                                    onClick={e => this.loadTripsAsync(this.state.page.number - 1, this.state.query === '' ? null : this.state.query)}>
                                {this.state.page.number}
                            </button>
                        </li> : null}
                        <li className={'page-item active'}>
                            <button className={'page-link'}
                                    onClick={e => this.loadTripsAsync(this.state.page.number, this.state.query === '' ? null : this.state.query)}>
                                {this.state.page.number + 1}
                            </button>
                        </li>
                        {!this.state.page.last ? <li className={'page-item'}>
                            <button className={'page-link'}
                                    onClick={e => this.loadTripsAsync(this.state.page.number + 1, this.state.query === '' ? null : this.state.query)}>
                                {this.state.page.number + 2}
                            </button>
                        </li> : null}
                        {this.state.page.number < this.state.page.totalPages - 2 ? <li className={'page-item'}>
                            {this.state.page.number < this.state.page.totalPages - 3 ? '...' : null}
                            <button className={'page-link'} onClick={e =>
                                this.loadTripsAsync(this.state.page.totalPages - 1, this.state.query === '' ? null : this.state.query)}>
                                {this.state.page.totalPages}
                            </button>
                        </li> : null}
                    </ul>
                    {this.state.page.content.length > 0 ? <>
                            <table className={'w-100'}>
                                <tbody>{this.state.page.content.map(trip =>
                                    <tr key={trip.id} data-id={trip.id}>
                                        <td>
                                            <div className={'mb-4 p-4'} style={{backgroundColor: '#d5d6ff'}}>
                                                <div className={'fs-4 fw-bold'}>{trip.title}</div>
                                                <div className={'d-flex justify-content-between'}
                                                     style={{marginBottom: '15px'}}>
                                                    <div>{trip.description}</div>
                                                    <div style={{minWidth: '250px'}}>
                                                        <div
                                                            className={'fw-semibold'}>Начало: {new Date(trip.startDate).toLocaleString()}</div>
                                                        <div
                                                            className={'fw-semibold'}>Конец: {new Date(trip.finishDate).toLocaleString()}</div>
                                                    </div>
                                                </div>
                                                <button className={'btn btn-info btn-sm'}
                                                        onClick={this.handleInfo}>
                                                    Информация
                                                </button>
                                            </div>
                                        </td>
                                    </tr>)}
                                </tbody>
                            </table>
                        </>
                        : 'Список поездок пуст'}
                </div>
                : <div className={'position-absolute d-flex justify-content-center align-items-center fw-bold fs-1'}
                       style={{width: '-webkit-fit-content', height: '-webkit-fit-content'}}>
                    Не уадалось получить информацию о поездках
                </div>
            }
        </div>;
    }
}

export default TripListComponent;

