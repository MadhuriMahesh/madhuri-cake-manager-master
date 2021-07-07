import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { instanceOf } from 'prop-types';
import { Cookies, withCookies } from 'react-cookie';

class CakeEdit extends Component {
    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    emptyItem = {
        title: '',
        description: '',
        image: ''
    };

    constructor(props) {
        super(props);
        const {cookies} = props;
        this.state = {
            item: this.emptyItem,
            csrfToken: cookies.get('XSRF-TOKEN')
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            try {
            const cake = await (await fetch(`/cake/${this.props.match.params.id}`,  {credentials: 'include'})).json();
            this.setState({item: cake});
            } catch (error) {
                this.props.history.push('/');
            }
        }
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item, csrfToken} = this.state;

        const requestOptions = {
            method: (item.id) ? 'PUT' : 'POST',
            headers: {
                'X-XSRF-TOKEN': csrfToken,
                'Content-Type': 'application/json' },
            body: JSON.stringify(item),
            credentials: 'include'
        };
        const response = await fetch('/cake' + (item.id ? '/' + item.id : ''), requestOptions);

        const data = await response.json();

        console.log(data)
        this.props.history.push('/cakeList');
    }

    render() {
        const {item} = this.state;
        const title = <h2>{item.id ? 'Edit Cake' : 'Add Cake'}</h2>;

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="title">Name</Label>
                        <Input type="text" name="title" id="title" value={item.title || ''}
                               onChange={this.handleChange} autoComplete="title"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="description">Description</Label>
                        <Input type="text" name="description" id="description" value={item.description || ''}
                               onChange={this.handleChange} autoComplete="description"/>
                    </FormGroup>

                    <FormGroup>
                        <Label for="image">Image (Please paste the URL of the Image)</Label>
                        <Input type="text" name="image" id="image" value={item.image || ''}
                               onChange={this.handleChange} autoComplete="image"/>
                    </FormGroup>
                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/cakes">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}

export default withCookies(withRouter(CakeEdit));