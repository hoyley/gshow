import React from "react";
import WelcomeScreen from "./Welcome";
import PlayerList from "./PlayerList";
import ChoiceGame from "./choiceGame/ChoiceGame";
import AdminPanel from "./AdminPanel";
import './Screen.css';
import {Container, Row, Col} from 'react-bootstrap';

const getMainScreen = (props) => {
  if (props.screen === "ChoiceGame") {
    return <ChoiceGame gameStatus={props.gameStatus}
                       gameConfig={props.gameConfig}
                       players={props.players}
                       myPlayer={props.myPlayer} />;
  } else {
    if (props.screen !== "Welcome") {
      console.log("Can't render screen named: " + props.screen);
    }
    return <WelcomeScreen/>;
  }
}

export default (props) => {
  return <Container className="screen" fluid={true}>
    { props.admin &&
        <AdminPanel className="adminPanel"
                    currentGameIndex={props.gameConfig.currentQuestion} />
    }
    <Row className="layoutRow">
      <Col className="layoutCol" sm={12} md={8}>
        <div className="gameArea">
          <div className="gameAreaContent">
            { getMainScreen(props) }
          </div>
        </div>
      </Col>
      <Col className="layoutCol" sm={12} md={4}>
        <PlayerList players={props.players}
                    myPlayer={props.myPlayer}
                    isAdmin={props.admin}/>
      </Col>
    </Row>
  </Container>
  
}
