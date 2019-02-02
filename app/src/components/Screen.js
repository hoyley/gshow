import React from "react";
import WelcomeScreen from "./Welcome"
import Registration from "./Registration"
import ChoiceGame from "./ChoiceGame"
import Error from "./Error"


export default (props) => {
    if (props.screen === "Welcome") {
      return <WelcomeScreen />;
    } else if (props.screen === "ChoiceGame") {
      return <ChoiceGame />;
    } else if (props.screen === "Registration") {
      return <Registration players={props.players} myPlayer={props.myPlayer} />;
    } else {
      return <Error msg={"Unknown screen: " + props.screen} />
    }
}
