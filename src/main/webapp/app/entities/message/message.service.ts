import axios from 'axios';

import { IMessage } from '@/shared/model/message.model';

const baseApiUrl = 'api/messages';

export default class MessageService {
  public find(id: number): Promise<IMessage> {
    return new Promise<IMessage>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieve(clusterDbId: number, topicName: string): Promise<any> {    
    console.log('msg service retrieve' + clusterDbId + topicName)
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${clusterDbId}/${topicName}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public create(clusterDbId: number, entity: IMessage): Promise<IMessage> {
    return new Promise<IMessage>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}/${clusterDbId}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public update(entity: IMessage): Promise<IMessage> {
    return new Promise<IMessage>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
