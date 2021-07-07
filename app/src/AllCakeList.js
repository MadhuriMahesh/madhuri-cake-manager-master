
import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class AllCakeList extends Component {

    constructor(props) {
        super(props);
        this.state = {cakes: [], isLoading: true};
        this.remove = this.remove.bind(this);
    }

    async componentDidMount() {
        this.setState({isLoading: true});

        const response = await  fetch('/allCakes');
        const body =  await response.json();
        this.setState({ cakes: body, isLoading: false });
    }

    async remove(id) {
        alert("delete??")

        const requestOptions = {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        };

        console.log("Id: " + id);
        await fetch('/cake/' + id, requestOptions)
            .then(() => {
                let updatedCakes = [...this.state.cakes].filter(i => i.id !== id);
                this.setState({cakes: updatedCakes});
            });
    }

    render() {
        const {cakes, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const cakeList = cakes.map(cake => {
            return <tr key={cake.id}>
                <td><img src={cake.image} /></td>
                <td style={{whiteSpace: 'nowrap'}}>{cake.title}</td>
                <td>{cake.description}</td>
                <ButtonGroup>
                    <Button size="sm" color="primary" tag={Link} to={"/cake/" + cake.id}>Edit</Button>
                    <Button size="sm" color="danger" onClick={() => this.remove(cake.id)}>Delete</Button>
                </ButtonGroup>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <span></span>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/cake/new">Add Cake</Button>
                    </div>
                    <h3><Button color="link"><Link to="/cakeList">User Cake</Link></Button> All Cakes</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="20%"></th>
                            <th width="20%">Name</th>
                            <th width="20%">Description</th>
                            <th width="10%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {cakeList}
                        </tbody>
                    </Table>
                    <div className="float-right">
                        <Button
                            type="button"
                            href={`data:text/json;charset=utf-8,${encodeURIComponent(
                                JSON.stringify(this.state.cakes)
                            )}`}
                            download="Cakes.json"
                        >
                            {`Download Json`}
                        </Button>
                    </div>
                </Container>
            </div>
        );
    }
}

export default AllCakeList;