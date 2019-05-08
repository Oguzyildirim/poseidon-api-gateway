import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product-category.reducer';
import { IProductCategory } from 'app/shared/model/product-category.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductCategoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ProductCategoryDetail extends React.Component<IProductCategoryDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { productCategoryEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="poseidonApiGatewayApp.productCategory.detail.title">ProductCategory</Translate> [
            <b>{productCategoryEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="levelOne">
                <Translate contentKey="poseidonApiGatewayApp.productCategory.levelOne">Level One</Translate>
              </span>
            </dt>
            <dd>{productCategoryEntity.levelOne}</dd>
            <dt>
              <span id="levelTwo">
                <Translate contentKey="poseidonApiGatewayApp.productCategory.levelTwo">Level Two</Translate>
              </span>
            </dt>
            <dd>{productCategoryEntity.levelTwo}</dd>
            <dt>
              <span id="levelThree">
                <Translate contentKey="poseidonApiGatewayApp.productCategory.levelThree">Level Three</Translate>
              </span>
            </dt>
            <dd>{productCategoryEntity.levelThree}</dd>
          </dl>
          <Button tag={Link} to="/entity/product-category" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/product-category/${productCategoryEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ productCategory }: IRootState) => ({
  productCategoryEntity: productCategory.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProductCategoryDetail);
