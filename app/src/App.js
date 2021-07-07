import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import CakeLists from "./CakeLists";
import CakeEdit from "./CakeEdit";
import { CookiesProvider } from 'react-cookie';
import AllCakeList from "./AllCakeList";

class App extends Component {
  render() {
    return (
        <CookiesProvider>
        <Router>
          <Switch>
            <Route path='/' exact={true} component={Home}/>
            <Route path='/cakeList' exact={true} component={CakeLists}/>
            <Route path='/cake/:id' component={CakeEdit}/>
            <Route path='/allCakeList' exact={true} component={AllCakeList}/>
          </Switch>
        </Router>
        </CookiesProvider>
    )
  }
}
export default App;