import React, { Component } from "react";
import service from "../service/GshowService";
import { StyleSheet, FlatList, Button, View, Text } from 'react-native';

export default class extends Component {

  onOptionPress(option) {
    service.choiceGameAnswer(option.option)
  }

  render() {
    const remainingTime = this.props.appState.gameStatus.remainingTime;
    const remainingPoints = this.props.appState.gameStatus.remainingPoints;
    const question = this.props.appState.gameConfig.question;
    const options = this.props.appState.gameConfig.options;
    const player = this.props.appState.myPlayer && this.props.appState.myPlayer.nickname;

    return <View>
      <Text>{"Player: " + player}</Text>
      <Text>{"Remaining Time: " + remainingTime}</Text>
      <Text>{"Remaining Points: " + remainingPoints}</Text>
      <Text>{question}</Text>

      <FlatList data={options}
                renderItem={
                  (item) => <Button title={item.item.option}
                                    onPress={() => this.onOptionPress(item.item)} />
                }
                />
    </View>
  }
}
