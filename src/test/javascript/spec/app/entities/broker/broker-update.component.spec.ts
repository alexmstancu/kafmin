/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import BrokerUpdateComponent from '@/entities/broker/broker-update.vue';
import BrokerClass from '@/entities/broker/broker-update.component';
import BrokerService from '@/entities/broker/broker.service';

import ClusterService from '@/entities/cluster/cluster.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('Broker Management Update Component', () => {
    let wrapper: Wrapper<BrokerClass>;
    let comp: BrokerClass;
    let brokerServiceStub: SinonStubbedInstance<BrokerService>;

    beforeEach(() => {
      brokerServiceStub = sinon.createStubInstance<BrokerService>(BrokerService);

      wrapper = shallowMount<BrokerClass>(BrokerUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          brokerService: () => brokerServiceStub,

          clusterService: () => new ClusterService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.broker = entity;
        brokerServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(brokerServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.broker = entity;
        brokerServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(brokerServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
