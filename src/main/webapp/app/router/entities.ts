import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const Broker = () => import('@/entities/broker/broker.vue');
// prettier-ignore
const BrokerUpdate = () => import('@/entities/broker/broker-update.vue');
// prettier-ignore
const BrokerDetails = () => import('@/entities/broker/broker-details.vue');
// prettier-ignore
const Cluster = () => import('@/entities/cluster/cluster.vue');
// prettier-ignore
const ClusterUpdate = () => import('@/entities/cluster/cluster-update.vue');
// prettier-ignore
const ClusterDetails = () => import('@/entities/cluster/cluster-details.vue');
// prettier-ignore
const Topic = () => import('@/entities/topic/topic.vue');
// prettier-ignore
const TopicUpdate = () => import('@/entities/topic/topic-update.vue');
// prettier-ignore
const TopicDetails = () => import('@/entities/topic/topic-details.vue');
// prettier-ignore
const Message = () => import('@/entities/message/message.vue');
// prettier-ignore
const MessageUpdate = () => import('@/entities/message/message-update.vue');
// prettier-ignore
const MessageDetails = () => import('@/entities/message/message-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/broker',
    name: 'Broker',
    component: Broker,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/broker/new',
    name: 'BrokerCreate',
    component: BrokerUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/broker/:brokerId/edit',
    name: 'BrokerEdit',
    component: BrokerUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/broker/:clusterId/:brokerId/view',
    name: 'BrokerView',
    component: BrokerDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/cluster',
    name: 'Cluster',
    component: Cluster,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/cluster/new',
    name: 'ClusterCreate',
    component: ClusterUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/cluster/:clusterId/edit',
    name: 'ClusterEdit',
    component: ClusterUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/cluster/:clusterId/view',
    name: 'ClusterView',
    component: ClusterDetails,
    meta: { authorities: [Authority.USER] },
  },

  {
    path: '/topic',
    name: 'Topic',
    component: Topic,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/topic/:clusterDbId/new',
    name: 'TopicCreate',
    component: TopicUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/topic/:clusterDbId/:topicName/edit',
    name: 'TopicEdit',
    component: TopicUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/topic/:clusterDbId/:topicName/view',
    name: 'TopicView',
    component: TopicDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/messages/:clusterDbId/:topicName',
    name: 'Message',
    component: Message,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/messages/:clusterDbId/:topicName/new',
    name: 'MessageCreate',
    component: MessageUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/message/:messageId/edit',
    name: 'MessageEdit',
    component: MessageUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/message/:messageId/view',
    name: 'MessageView',
    component: MessageDetails,
    meta: { authorities: [Authority.USER] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
