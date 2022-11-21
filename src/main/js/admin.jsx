import React from "react";
import ReactDOM from "react-dom/client"
import UserListComponent from "./components/user.jsx";
import TripListComponent from "./components/trip.jsx";
import ApplicationListComponent from "./components/application.jsx";
import "bootstrap/dist/css/bootstrap.min.css";

class AdminComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {tab: null};
    }

    render() {
        return <div style={{margin: '20px', border: '2px solid deepskyblue', width: 'calc(100% - 40px)'}}
                    className='grid-wrapper'>
            <div className='sidebar'>
                <div className={`btn ${this.state.tab === 'applications' ? 'active' : null}`} data-tab={'applications'}
                     onClick={e => this.setState({tab: e.target.dataset['tab']})}>
                    Заявки
                </div>
                <div className={`btn ${this.state.tab === 'trips' ? 'active' : null}`} data-tab={'trips'}
                     onClick={e => this.setState({tab: e.target.dataset['tab']})}>
                    Поездки
                </div>
                <div className={`btn ${this.state.tab === 'users' ? 'active' : null}`} data-tab={'users'}
                     onClick={e => this.setState({tab: e.target.dataset['tab']})}>
                    Пользователи
                </div>
            </div>
            <div className='grid-content'>
                {
                    {
                        'applications': <ApplicationListComponent/>,
                        'trips': <TripListComponent/>,
                        'users': <UserListComponent/>
                    }[this.state.tab]
                }
            </div>
        </div>
    }
}

ReactDOM.createRoot(document.getElementById("admin")).render(<AdminComponent/>);