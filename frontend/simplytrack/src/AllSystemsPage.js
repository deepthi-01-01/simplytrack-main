import React, { useState, useEffect } from 'react';
import logo from './logo.svg';
import {
  Container,
  Row,
  Col,
  Table,
  Navbar,
  Nav,
  NavDropdown,
} from 'react-bootstrap';
import Dashboard from './Dashboard'; // Import the Dashboard component

const AllSystemsPage = () => {
  const [systems, setSystems] = useState([]);
  const [selectedSystem, setSelectedSystem] = useState(null); // State to track selected system

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/system/all', {
          credentials: 'include',
        });
        const data = await response.json();
        setSystems(data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  const handleLogoutClick = (e) => {
    e.preventDefault();
    window.location.href = 'http://localhost:8080/logout';
  };
  const handleSystemClick = (systemId) => {
    // Set the selected system when clicked
    setSelectedSystem(systemId);
  };

  // Render Dashboard if a system is selected
  if (selectedSystem) {
    return <Dashboard systemId={selectedSystem} />;
  } else {
    return (
      <>
        <Navbar bg="dark" variant="dark" expand="lg">
          <Container>
            <Navbar.Brand href="#">
              <img
                src={logo}
                alt="Brand Logo"
                height="30"
                className="d-inline-block align-top"
              />
            </Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="me-auto">
                <Nav.Link
                  onClick={(e) => {
                    e.preventDefault();
                    setSelectedSystem(null);
                  }}
                >
                  Systems
                </Nav.Link>
                {/* <Nav.Link href="/about">About</Nav.Link>
              <Nav.Link href="/contact">Contact</Nav.Link> */}
              </Nav>
              <Nav>
                <NavDropdown
                  title={window.sessionStorage.getItem('username')}
                  id="user-account-dropdown"
                >
                  {/* <NavDropdown.Item href="/account">
                  Account Settings
                </NavDropdown.Item> */}
                  <NavDropdown.Divider />
                  <NavDropdown.Item onClick={handleLogoutClick}>
                    Logout
                  </NavDropdown.Item>
                </NavDropdown>
              </Nav>
            </Navbar.Collapse>
          </Container>
        </Navbar>
        <Container>
          <Row>
            <Col>
              <h1>All Systems</h1>
              <Table striped bordered hover>
                <thead>
                  <tr>
                    <th>System ID</th>
                    <th>Total CPU Usage %</th>
                    <th>Disk Usage(Bytes)</th>
                    <th>Memory Usage(MB )</th>
                  </tr>
                </thead>
                <tbody>
                  {systems.map((system, index) => (
                    <tr
                      key={index}
                      onClick={() => handleSystemClick(system.systemId)}
                      style={{ cursor: 'pointer' }}
                    >
                      <td>{system.systemId}</td>
                      <td>{system.totalCpuUsage}</td>
                      <td>{system.diskUsage}</td>
                      <td>{system.memoryUsage}</td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </Col>
          </Row>
        </Container>
      </>
    );
  }
};

export default AllSystemsPage;
