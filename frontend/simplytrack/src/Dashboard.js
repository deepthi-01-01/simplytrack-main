import React, { useState, useEffect } from 'react';
import logo from './logo.svg';
import {
  Container,
  Row,
  Col,
  Card,
  Pagination,
  Navbar,
  Nav,
  NavDropdown,
} from 'react-bootstrap';
import { Bar } from 'react-chartjs-2';
import 'chart.js/auto';

const Dashboard = ({ systemId }) => {
  const [data, setData] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/api/system/all?systemId=${systemId}`,
          {
            credentials: 'include',
          }
        );
        const data = await response.json();
        setData(data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, [systemId]);

  const systemInfoData = {
    labels: ['RAM', 'CPU'],
    datasets: [
      {
        label: 'System Info',
        data: [
          data.reduce((sum, system) => sum + system.memoryUsage, 0),
          data.reduce((sum, system) => sum + system.totalCpuUsage, 0),
        ],
        backgroundColor: 'rgba(75,192,192,0.2)',
        borderColor: 'rgba(75,192,192,1)',
        borderWidth: 1,
      },
    ],
  };
  const handleLogoutClick = (e) => {
    e.preventDefault();
    window.location.href = 'http://localhost:8080/logout';
  };

  const topCpuProcessesData = {
    labels: data
      .flatMap((system) => system.processInfos)
      .sort((a, b) => parseFloat(b.cpuUsage) - parseFloat(a.cpuUsage))
      .slice(0, 5)
      .map((process) => process.processName),
    datasets: [
      {
        label: 'CPU Usage',
        data: data
          .flatMap((system) => system.processInfos)
          .sort((a, b) => parseFloat(b.cpuUsage) - parseFloat(a.cpuUsage))
          .slice(0, 5)
          .map((process) => parseFloat(process.cpuUsage)),
        backgroundColor: 'rgba(75,192,192,0.2)',
        borderColor: 'rgba(75,192,192,1)',
        borderWidth: 1,
      },
    ],
  };

  const topRamProcessesData = {
    labels: data
      .flatMap((system) => system.processInfos)
      .sort(
        (a, b) =>
          parseFloat(b.memoryUsage.slice(0, -3)) -
          parseFloat(a.memoryUsage.slice(0, -3))
      )
      .slice(0, 5)
      .map((process) => process.processName),
    datasets: [
      {
        label: 'Memory Usage',
        data: data
          .flatMap((system) => system.processInfos)
          .sort(
            (a, b) =>
              parseFloat(b.memoryUsage.slice(0, -3)) -
              parseFloat(a.memoryUsage.slice(0, -3))
          )
          .slice(0, 5)
          .map((process) => parseFloat(process.memoryUsage.slice(0, -3))),
        backgroundColor: 'rgba(75,192,192,0.2)',
        borderColor: 'rgba(75,192,192,1)',
        borderWidth: 1,
      },
    ],
  };

  // Calculate the index of the last and first item based on the current page
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;

  // Slice the data array to get the data for the current page
  const currentData = data.slice(indexOfFirstItem, indexOfLastItem);

  // Update handlePageChange function to change the current page
  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  // Calculate total pages based on the total number of items and items per page
  const totalPages = Math.ceil(data.length / itemsPerPage);

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
                  window.location.reload();
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
        <Row className="mb-4 mt-4">
          <Col xs={12} md={6} lg={4}>
            <Card>
              <Card.Body>
                <Card.Title>CPU Usage</Card.Title>
                <h4>
                  {data.reduce((sum, system) => sum + system.totalCpuUsage, 0)}%
                </h4>
              </Card.Body>
            </Card>
          </Col>
          <Col xs={12} md={6} lg={4}>
            <Card>
              <Card.Body>
                <Card.Title>RAM Usage</Card.Title>
                <h4>
                  {data.reduce((sum, system) => sum + system.memoryUsage, 0)} MB
                </h4>
              </Card.Body>
            </Card>
          </Col>
          <Col xs={12} md={6} lg={4}>
            <Card>
              <Card.Body>
                <Card.Title>Disk Usage</Card.Title>
                <h4>
                  {data.reduce((sum, system) => sum + system.diskUsage, 0)}{' '}
                  Bytes
                </h4>
              </Card.Body>
            </Card>
          </Col>
        </Row>
        <Row className="mb-4">
          {/* <Col>
            <h2>System Info</h2>
            <Bar data={systemInfoData} />
          </Col> */}
        </Row>
        <Row className="mb-4">
          <Col md={6}>
            <h2>Top CPU Processes</h2>
            <Bar data={topCpuProcessesData} />
          </Col>
          <Col md={6}>
            <h2>Top RAM Processes</h2>
            <Bar data={topRamProcessesData} />
          </Col>
        </Row>
        {currentData.map((system, index) => (
          <React.Fragment key={index}>
            <Row className="mb-4">
              <Col>
                <h2>System Info</h2>
                <table className="table table-striped table-bordered table-hover">
                  <thead>
                    <tr>
                      <th>System ID</th>
                      <th>Total CPU Usage %</th>
                      <th>Disk Usage(Bytes)</th>
                      <th>Memory Usage(MB)</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>{system.systemId}</td>
                      <td>{system.totalCpuUsage}</td>
                      <td>{system.diskUsage}</td>
                      <td>{system.memoryUsage}</td>
                    </tr>
                  </tbody>
                </table>
              </Col>
            </Row>
            <Row className="mb-4">
              <Col>
                <h2>Process Info</h2>
                <table className="table table-striped table-bordered table-hover">
                  <thead>
                    <tr>
                      <th>PID</th>
                      <th>Process Name</th>
                      <th>CPU Usage</th>
                      <th>Memory Usage</th>
                    </tr>
                  </thead>
                  <tbody>
                    {system.processInfos.map((process, processIndex) => (
                      <tr key={processIndex}>
                        <td>{process.pid}</td>
                        <td>{process.processName}</td>
                        <td>{process.cpuUsage}</td>
                        <td>{process.memoryUsage}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </Col>
            </Row>
          </React.Fragment>
        ))}
        <Row>
          <Col>
            <Pagination>
              {Array.from({ length: totalPages }, (_, i) => i + 1).map(
                (pageNumber) => (
                  <Pagination.Item
                    key={pageNumber}
                    active={pageNumber === currentPage}
                    onClick={() => handlePageChange(pageNumber)}
                  >
                    {pageNumber}
                  </Pagination.Item>
                )
              )}
            </Pagination>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Dashboard;
